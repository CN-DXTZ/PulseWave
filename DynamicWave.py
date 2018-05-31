import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import MySQL
import SerialData
import _thread


class Scope(object):
    def __init__(self, ax, cur, tableName, rang=400, time=0):
        self.rang = rang
        self.ax = ax
        self.cur = cur
        self.time = time
        self.xdata = np.arange(self.rang)
        self.ydata = np.array(np.zeros(self.rang))
        self.line, = ax.plot(self.xdata, self.ydata)
        self.ax.set_xlim(0, self.rang)
        self.tableName = tableName

    def update(self, frame):
        sql = 'select * from %s where time>%s limit 10' % (self.tableName, self.time)
        self.cur.execute(sql)
        num = 0
        for item in self.cur.fetchall():
            # if (num < 3):
            num = num + 1
            # else:
            #     break
            self.time = item[0]
            self.ydata = np.append(self.ydata, [item[1]], axis=0)
        self.ydata = self.ydata[num:]
        self.line.set_ydata(self.ydata)
        self.ax.set_ylim(self.ydata.min(), self.ydata.max())
        return self.line,


def Display(tableName):
    fig, ax = plt.subplots()
    # plt.xticks([])
    cur = MySQL.MysqlInit()
    scope = Scope(ax, cur, tableName, rang=800, time=0)
    ani = animation.FuncAnimation(fig, scope.update, interval=0, blit=True)
    plt.show()


if __name__ == '__main__':
    Display(11)
