clc; clear;close all;
load('xSpilit.mat')

xSpilit=xSpilit.*200;
%% –°≤®»•‘Î
len =size(xSpilit,1);
xOrigin=zeros(len,200);
yTrain=zeros(len,1);
% threshold=[41, 237];
flag=0.0;
for i=[1,140,255] %1:len
%     if(find(threshold==i))
%         flag=flag+1;
%         figure
%     end
    figure
    hold on
    axis([0 180 -10 180]) 
    p=find(xSpilit(i,:)==0,1)-1;
    xi=xSpilit(i,1:p);
    haar = wden(xi,'modwtsqtwolog','s','mln',3,'haar');
    xOrigin(i,1:size(haar,2))=haar;
    yTrain(i,1)=flag;
    plot(haar)
end

% save('xOrigin.mat','xOrigin')
% save('yTrain.mat','yTrain')
