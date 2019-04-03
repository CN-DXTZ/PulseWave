clc; clear;close all;
db = database('wave', 'root', '1234',...
    'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/wave');
curs = exec(db, 'select * from dengbin_20180531_01_style1');
curs = fetch(curs)  ;
ress = curs.Data;  
ress = cell2mat(ress);  
ress = ress(1000:14000,2);
len=length(ress);
y2=zeros(len,1);
y2(1)=ress(1);
thr=0.75;
for i=2:len
    y2(i)=y2(i-1)*thr+ress(i)*(1-thr);
end
hold on;
plot(ress)
plot(y2)

figure
hold on;
