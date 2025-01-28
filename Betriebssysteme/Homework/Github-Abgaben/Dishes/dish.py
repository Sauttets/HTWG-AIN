from time import monotonic
from multiprocessing import RLock, current_process
from multiprocessing.sharedctypes import Array
from ctypes import Structure, c_int, c_bool, c_uint8
from debug import debug


class Dish(Structure):
    _fields_ = [('pid', c_int),
                ('has_been_eaten', c_bool),
                ('animal_type', c_uint8),
                ('is_empty', c_bool)]


class Dishes:
    @debug
    def __init__(self, size):
        self.size = size
        self.__data = Array(Dish, [(1, False, 1, True)]*size, lock=RLock())

    @debug
    def __len__(self):
        return self.size

    @debug
    def __getitem__(self, pid):
        with self.__data:
            for data in self.__data:
                if not data.is_empty and data.pid == pid:
                    return data
        return None

    @debug
    def __delitem__(self, pid):
        with self.__data:
            for data in self.__data:
                if not data.is_empty and data.pid == pid:
                    data.is_empty = True
                    return

    @debug
    def __contains__(self, pid):
        with self.__data:
            for data in self.__data:
                if not data.is_empty and data.pid == pid and not data.has_been_eaten:
                    return True
        return False

    @debug
    def was_i_eaten(self, pid):
        with self.__data:
            for data in self.__data:
                if not data.is_empty and data.pid == pid and data.has_been_eaten:
                    return True
        return False

    @debug
    def eat_rather_another_animal(self, animal_type):
        with self.__data:
            for data in self.__data:
                if not data.is_empty and data.animal_type == animal_type:
                    data.has_been_eaten = True
                    return True
        return False

    @debug
    def remove(self, pid):
        self.__delitem__(pid)

    @debug
    def place(self, pid, animal_type):
        with self.__data:
            for data in self.__data:
                if not data.is_empty and data.pid == pid:
                    return True
            for data in self.__data:
                if data.is_empty:
                    data.pid = pid
                    data.animal_type = animal_type
                    data.is_empty = False
                    data.has_been_eaten = False
                    return True
        return False
