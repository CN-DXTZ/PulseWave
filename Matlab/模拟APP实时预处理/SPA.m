clc; clear;close all;

% n = 0:1/200:(1024-1)/200;
% data = sin(2*pi*0.1*n)+sin(2*pi*5*n);
% data = data(:);

A = textread('app4.txt');
A=A(1:10000);
data = -(A');

% db = database('wave', 'root', '1234',...
%     'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/wave');
% curs = exec(db, 'select * from data1');
% curs = fetch(curs)  ;
% ress = curs.Data;  
% ress = cell2mat(ress);  
% data = -(ress(5000:15000,2)); % 4100:95527


N = length(data);
n = 0:1/200:(N-1)/200;

lambda = 10000;

I = speye(N);

D2 = spdiags(ones(N-2,1)*[1 -2 1], [0 1 2], N-2, N);

    trend = inv(I+lambda^2*(D2'*D2))*data;

detrenddata = data-trend;
subplot(211);
% figure
plot(n,data,'b',n,trend,'r');
set( gca ,'FontSize',20);
ylabel('振幅','FontSize',20)
xlabel('时间/s','FontSize',20)
title('原始脉搏波和基线漂移','FontSize',24);
legend({'原始脉搏波','基线漂移'},'FontSize',24);


subplot(212);
% figure
plot(n,detrenddata)
ylabel('振幅','FontSize',20)
xlabel('时间/s','FontSize',20)
set( gca ,'FontSize',20);
title('基线漂移校正后','FontSize',24);
