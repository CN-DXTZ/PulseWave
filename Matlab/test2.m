clc; clear;close all;
load('xOrigin.mat')
[m,n]=size(xOrigin);
valAbsMax=zeros(m,4);
deriAbsMax=zeros(m,4);
y=zeros(m,7);
for i = 1:m % [2 5 48 58  300 310] % 
    % 首峰
    valAbsMax(i,1)=find(xOrigin(i,:)==max(xOrigin(i,:)));
    % 末尾
    valAbsMax(i,4)=find(xOrigin(i,:)==0,1);
    % 首谷，次峰
    diff=xOrigin(i,2:n)-xOrigin(i,1:n-1); 
    num=1;
    for j= valAbsMax(i,1)+1:ceil(valAbsMax(i,4)*0.8)
        flag=(diff(j-1)>0)+(diff(j)>0);
        if(flag==1)
            if(num==1)
                num=2;
                val1=j;
            elseif(num ==2)
                valAbsMax(i,2)=val1;
                valAbsMax(i,3)=j;
            end
        end
    end

    % 首峰斜率最大
    deriAbsMax(i,1)=find(diff==max(diff));
    deriAbsMax(i,2)=find(diff==min(diff));
    % 次峰斜率最大
    if(valAbsMax(i,2)~=0)
        base=valAbsMax(i,2);
        newX=xOrigin(i,base+1:2*valAbsMax(i,3)-base);
        diff=newX(2:length(newX))-diff(1:length(newX)-1);
        deriAbsMax(i,3)=base+find(diff==max(diff));
        deriAbsMax(i,4)=base+find(diff==min(diff));
    end

%     hold on;
%     plot(xOrigin(i,:))
%     plot(valAbsMax(i,1),xOrigin(i,valAbsMax(i,1)),'r.','markersize',16)
%     plot(valAbsMax(i,4),xOrigin(i,valAbsMax(i,4)),'b.','markersize',16)
%     plot(deriAbsMax(i,1),xOrigin(i,deriAbsMax(i,1)),'g+','LineWidth',2)
%     plot(deriAbsMax(i,2),xOrigin(i,deriAbsMax(i,2)),'y+','LineWidth',2)
%     if(valAbsMax(i,2)~=0)
%     plot(valAbsMax(i,2),xOrigin(i,valAbsMax(i,2)),'c.','markersize',16)
%     plot(valAbsMax(i,3),xOrigin(i,valAbsMax(i,3)),'m.','markersize',16)
%     plot(deriAbsMax(i,3),xOrigin(i,deriAbsMax(i,3)),'g+','LineWidth',2)
%     plot(deriAbsMax(i,4),xOrigin(i,deriAbsMax(i,4)),'y+','LineWidth',2)
%     end

    y(i,1)=xOrigin(i,valAbsMax(i,1));
    y(i,4:5)=(xOrigin(i,deriAbsMax(i,[1 2]))+xOrigin(i,deriAbsMax(i,[1 2])+1))./2;
    if(valAbsMax(i,2)~=0)
        y(i,2:3)=xOrigin(i,valAbsMax(i,[2 3]));
        y(i,6:7)=(xOrigin(i,deriAbsMax(i,[3 4]))+xOrigin(i,deriAbsMax(i,[3 4])+1))./2;
    end
    
end

load('yTrain.mat')
% data=[valAbsMax,deriAbsMax,y,yTrain];

% valAbsMax(1:4)=极值x
% deriAbsMax(5:8)=侧值x
% y(9:15)=上述x对应y（省略valAbsMax(4),最后极小值都为0）
data=[valAbsMax,deriAbsMax,y,yTrain];
save('data.mat','data')
