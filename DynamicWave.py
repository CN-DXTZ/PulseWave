import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import MySQL


class Scope(object):
    def __init__(self, ax, cur, rang=400, time=0):
        self.rang = rang
        self.ax = ax
        self.cur = cur
        self.time = time
        self.xdata = np.arange(self.rang)
        self.ydata = np.array(np.zeros(self.rang))
        self.line, = ax.plot(self.xdata, self.ydata)
        self.ax.set_xlim(0, self.rang)

    def update(self, frame):
        sql = 'select * from wavedata where time>%s' % (self.time)
        self.cur.execute(sql)
        num = 0
        for item in self.cur.fetchall():
            if (num < 3):
                num = num + 1
            else:
                break
            self.time = item[0]
            self.ydata = np.append(self.ydata, [item[1]], axis=0)
        self.ydata = self.ydata[num:]
        self.line.set_ydata(self.ydata)
        self.ax.set_ylim(self.ydata.min(), self.ydata.max())
        return self.line,


def Display():
    fig, ax = plt.subplots()
    # plt.xticks([])
    cur = MySQL.MysqlInit()
    scope = Scope(ax, cur, rang=1000, time=0)
    ani = animation.FuncAnimation(fig, scope.update, interval=0, blit=True)
    plt.show()


if __name__ == '__main__':
    Display()
