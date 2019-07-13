clc; clear;close all;
load('xSpilit.mat')

xSpilit=xSpilit.*200;
%% 小波去噪
len =size(xSpilit,1);
xTrain=zeros(len,200);
yTrain=zeros(len,1);
threshold=[41, 237];
flag=0.0;
set( gca ,'FontSize',20);
ylabel('振幅','FontSize',20)
xlabel('时间/s','FontSize',20)
% for i=1:len
for i=[10,11,12,156,157,158,255,256,257] 
    
    % 标记值
    if(find(threshold==i))
        flag=flag+1;
%         figure
    end

%     figure
    hold on
    axis([0 1 -10 180]) 
    p=find(xSpilit(i,:)==0,1)-1;
    xi=xSpilit(i,1:p);
    haar = wden(xi,'modwtsqtwolog','s','mln',3,'haar');
    
    
    xTrain(i,1:size(haar,2))=haar;
    yTrain(i,1)=flag;
    
%     if flag==0
%         plot(haar,'-k','linewidth',2)
%     elseif flag==1
%         plot(haar,'-r')
%     elseif flag==2
%         plot(haar,'--b')
%     end
    xxxx=0:0.005:length(haar)/200.0-0.005;
    if i<40
        p1=plot(xxxx,haar,'-k','linewidth',1.5);
    elseif i<237
        p2=plot(xxxx,haar,'-xr','linewidth',1.5);
    else
        p3=plot(xxxx,haar,'--b','linewidth',1.5);
    end
end
legend([p1,p2,p3],'平稳','运动','休息')

% save('xTrain.mat','xTrain')
% save('yTrain.mat','yTrain')
