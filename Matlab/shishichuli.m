clc; clear;close all;
db = database('wave', 'root', '1234',...
    'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/wave');
curs = exec(db, 'select * from dengbin_20180531_01_style1');
curs = fetch(curs)  ;
ress = curs.Data;  
ress = cell2mat(ress);  
ress = ress(:,2);
len=length(ress);
plot(ress);