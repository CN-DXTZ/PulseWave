sql = "insert into %s values " % ("3123123312")
t=0
for i in range(5):
    t = t + 1
    sql += "(%d,%d)," % (t, -t)
sql = sql[:-1]
print(sql)
