% clc; clear;close all;

% tt=0;
x=(1:255)';
y=tt;
plot(x,y)
figure
x1=0:0.01:255;
y1=interp1(x,y,x1,'Spline');
plot(x1,y1)

% db = database('wave', 'root', '1234',...
%     'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/wave');
% curs = exec(db, 'select * from dengbin_20180531_01_style1');
% curs = fetch(curs)  ;
% ress = curs.Data;  
% ress = cell2mat(ress);  
% ress = ress(1000:14000,2);

