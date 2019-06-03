clc; clear;close all;

% n = 0:1/200:(1024-1)/200;
% data = sin(2*pi*0.1*n)+sin(2*pi*5*n);
% data = data(:);

A = textread('app4.txt');
data = -(A(4000:9000)');
n = 0:1/200:(length(data)-1)/200;

N = length(data);

lambda = 2500;

I = speye(N);

D2 = spdiags(ones(N-2,1)*[1 -2 1], [0 1 2], N-2, N);

trend = inv(I+lambda^2*D2'*D2)*data;

detrenddata = data-trend;
subplot(211);
% figure
plot(n,data,'r',n,trend,'g');
title('the orginal data and trend');
legend('the orginal data','the trend');


subplot(212);
% figure
plot(n,detrenddata,'m')
title('the data after detrenging');
