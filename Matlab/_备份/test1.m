clc; clear;close all;
load('xOrigin.mat')
[m,n]=size(xOrigin);
dataX=zeros(m,4);
dataY=zeros(m,3);
dataX(:,1)=dataX(:,1)+1;
diff=zeros(1,n-1);
threshold=[41, 237];
for i = 1:m
%     if(find(threshold==i))
%         figure
%     end
    
    % 顶峰
    dataX(i,3)=find(xOrigin(i,:)==max(xOrigin(i,:)));
    % 结尾
    dataX(i,1)=find(xOrigin(i,:)==0,1);
    % 斜率极值
    diff=xOrigin(i,2:n)-xOrigin(i,1:n-1); 
    dataX(i,2)=find(diff==max(diff));
    dataX(i,4)=find(diff==min(diff));
    
%     hold on;
%     axis([0 180 -0.1 1])
%     plot(xOrigin(i,:))
%     plot(dataX(i,2),xOrigin(i,dataX(i,2)),'g.','markersize',16)
%     plot(dataX(i,3),xOrigin(i,dataX(i,3)),'r.','markersize',16)
%     plot(dataX(i,4),xOrigin(i,dataX(i,4)),'y.','markersize',16)
%     plot(dataX(i,1),xOrigin(i,dataX(i,1)),'b.','markersize',16)
    
    dataY(i,2)=xOrigin(i,dataX(i,3));
    dataY(i,1)=(xOrigin(i,dataX(i,2))+xOrigin(i,dataX(i,2)+1))./2;
    dataY(i,3)=(xOrigin(i,dataX(i,4))+xOrigin(i,dataX(i,4)+1))./2;
end



load('yTrain.mat')
data=[dataX,dataY,yTrain];
% data=[dataX,dataY(:,2:3),yTrain];
save('data.mat','data')
