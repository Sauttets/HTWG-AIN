import time
from enum import IntEnum
from time import monotonic
from multiprocessing import Condition, RLock, Process, current_process
from multiprocessing.sharedctypes import Array, Value
from ctypes import c_float, c_uint8
import multiprocessing
from debug import debug
from dish import Dishes, Dish
import logging


class State(IntEnum):
    SATISFIED = 0
    EATING = 1
    STRAVING = 2
    DEAD = 3


class AnimalType(IntEnum):
    MOUSE = 0
    CAT = 1
    DOG = 2
    IDK = 3


# Readme
# https://docs.python.org/3/library/multiprocessing.html#the-process-class
class Animal(Process):
    EATS_ANIMAL_TYPE = AnimalType.IDK
    ANIMAL_TYPE = AnimalType.IDK

    def __init__(self, dishes, satisfied_time_s, eat_time_s, life_time_s, barrier):
        # Readme
        # https://docs.python.org/3/library/multiprocessing.html#multiprocessing.Array
        self.status_time_stamp_s = multiprocessing.Array(c_float, 4, lock=RLock())
        self.status_time_remaining_s = multiprocessing.Array(c_float, 4, lock=RLock())
        self.dishes = dishes
        self.dish_context = None
        self.barrier = barrier
        # Readme
        # https://docs.python.org/3/library/threading.html#lock-objects
        self.cv_lock = RLock()
        # Readme
        # https://docs.python.org/3/library/threading.html#threading.Condition
        self.condition = Condition(self.cv_lock)
        # Readme
        # https://docs.python.org/3/library/multiprocessing.html#multiprocessing.Value
        self.status = Value(c_uint8, lock=RLock())
        self.lived_for = Value(c_float, lock=RLock())
        # state machine definition
        self.statuses_transaction = {State.EATING: State.SATISFIED,
                                     State.SATISFIED: State.STRAVING,
                                     State.STRAVING: State.DEAD,
                                     State.DEAD: State.DEAD}
        self.status_resets = {State.EATING: eat_time_s,
                              State.SATISFIED: satisfied_time_s,
                              State.STRAVING: life_time_s,
                              State.DEAD: 0.}
        # init
        # Animal is satisfied at the beginning.
        self._set_status(State.SATISFIED)
        # At the end, the lifetime is calculated via the delta.
        self.status_time_stamp_s[State.DEAD] = monotonic()
        # is updated after sleep
        # Counts down while eating.
        # Restarted if satisfied.
        for state in State:
            self.status_time_remaining_s[state] = self.status_resets[state]

        super(Animal, self).__init__()
        self.start()

    @debug
    def __can_eat(self):
        return self.dish_context.pid in self.dishes

    @debug
    def __was_i_eaten(self):
        if self.dishes.was_i_eaten(self.dish_context.pid):
            self._set_status(State.DEAD)

    @debug
    def goto_dishes(self):

        if self.get_status() == State.DEAD:
            logging.warning("%s is DEAD!" % self.name)
            return False

        if self.pid in self.dishes:
            logging.warning("%s is already placed, time: %s" % (self.name, monotonic()))
            return True

        if self.dishes.eat_rather_another_animal(self.EATS_ANIMAL_TYPE):
            logging.warning("%s eating another animal instead at time: %s" % (self.name, monotonic()))
            self._set_status(State.EATING)
            return True

        if self.dishes.place(self.pid, self.ANIMAL_TYPE):
            logging.info("%s is going to the dishes at time: %s" % (self.name, monotonic()))
            return True

        logging.warning("%s no space left at the dishes, time: %s" % (self.name, monotonic()))
        return False

    @debug
    def get_time_stamps_s(self):
        with self.status_time_stamp_s.get_lock():
            return self.status_time_stamp_s

    @debug
    def leave_dish(self):
        self.dishes.remove(self.dish_context.pid)

    @debug
    def wakeup(self):
        with self.cv_lock:
            self.condition.notify()

    @debug
    def get_status(self):
        with self.status.get_lock():
            return self.status.value

    @debug
    def get_remaining_status_times_s(self):
        with self.status_time_remaining_s.get_lock():
            return self.status_time_remaining_s

    @debug
    def get_lifetime(self):
        with self.status_time_stamp_s.get_lock():
            if self.get_status() != State.DEAD:
                return monotonic() - self.status_time_stamp_s[State.DEAD]
            else:
                with self.lived_for.get_lock():
                    return self.lived_for.value

    @debug
    def _set_status(self, status):
        with self.status.get_lock():
            self.status.value = status
        self.wakeup()

    @debug
    def __state_transition(self):
        with self.status_time_stamp_s.get_lock(), self.status_time_remaining_s.get_lock():
            logging.info("%s in %s, time: %s" % (self.name, State(self.get_status()), monotonic()))
            self.status_time_stamp_s[self.get_status()] = monotonic() + self.status_time_remaining_s[self.get_status()]
            logging.info("%s should wake up at the time: %s" % (self.name, self.status_time_stamp_s[self.get_status()]))
            wait_time = self.status_time_remaining_s[self.get_status()]
        with self.cv_lock:
            if not self.condition.wait(wait_time) or wait_time <= 0.:
                logging.info("%s woken up by timeout at the time: %s" % (self.name, monotonic()))
                with self.status_time_remaining_s.get_lock():
                    self.status_time_remaining_s[self.get_status()] = self.status_resets[self.get_status()]
                self._set_status(self.statuses_transaction[self.get_status()])
            else:
                with self.status_time_stamp_s.get_lock(), self.status_time_remaining_s.get_lock():
                    self.status_time_remaining_s[self.get_status()] = self.status_time_stamp_s[self.get_status()] - monotonic()
                    if self.status_time_remaining_s[self.get_status()] < 0.:
                        self.status_time_remaining_s[self.get_status()] = 0.
                    logging.info("%s woken up by signal (remaining time: %s) at the time: %s" % (self.name, self.status_time_remaining_s[self.get_status()], monotonic()))

    def run(self):
        self.dish_context = Dish()
        self.dish_context.pid = current_process().ident
        self.dish_context.animal_type = self.ANIMAL_TYPE
        self.barrier.wait()
        logging.info("%s is ALIVE at time: %s" % (self.name, monotonic()))
        while self.get_status() != State.DEAD:
            if self.get_status() != State.SATISFIED and self.__can_eat():
                self._set_status(State.EATING)
            if self.get_status() != State.EATING:
                self.leave_dish()
            self.__state_transition()
            self.__was_i_eaten()
        self.leave_dish()
        with self.lived_for.get_lock(), self.status_time_stamp_s.get_lock():
            self.lived_for.value = monotonic() - self.status_time_stamp_s[State.DEAD]
            logging.info("%s is DEAD and has lived @ %f" % (self.name, self.lived_for.value))


class Dog(Animal):
    EATS_ANIMAL_TYPE = AnimalType.CAT
    ANIMAL_TYPE = AnimalType.DOG


class Cat(Animal):
    EATS_ANIMAL_TYPE = AnimalType.MOUSE
    ANIMAL_TYPE = AnimalType.CAT


class Mouse(Animal):
    EATS_ANIMAL_TYPE = AnimalType.IDK
    ANIMAL_TYPE = AnimalType.MOUSE
