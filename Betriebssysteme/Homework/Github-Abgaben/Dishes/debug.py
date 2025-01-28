import logging
from multiprocessing import current_process
from time import monotonic


# annotation
# https://docs.python.org/3/howto/annotations.html
def debug(func, *objs):
    def debug_msg(*args, **kwargs):
        pid = current_process().ident
        name = current_process().name
        # nor black or withe
        color = (pid % 2), (pid % 6) + 31
        time_start = monotonic()
        logging.debug(">>(\033[%s;%sm%s [%s]\033[0m) %s time:%s" % (color[0], color[1], name, pid, func.__name__, time_start))
        result = func(*args, **kwargs)
        logging.debug("(\033[%s;%sm%s [%s]\033[0m)>> %s time:%s runtime:%s" % (color[0], color[1], name, pid, func.__name__, monotonic(), monotonic()-time_start))
        return result
    return debug_msg
