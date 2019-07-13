clc; clear;close all;
db = database('wave', 'root', '1234',...
    'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/wave');
curs = exec(db, 'select * from data1');
% data1 分段
% 1:6000 平稳  0:00-0:30
% 6000:18000 运动1  0:30-
% 18000:30000 运动2     -2:30
% 30000:66268 休息  2:30-5:30
% data2 休息
curs = fetch(curs)  ;
ress = curs.Data;  
ress = cell2mat(ress);  
ress = ress(:,2); % 4100:95527
% length(ress)
% plot(ress)
% return



%% 小波去噪
yn=ress;
len =length(yn);
haar = wden(yn,'modwtsqtwolog','s','mln',4,'haar');

% % % 画全图
figure;
hold on

% set( gca ,'FontSize',20);
% ylabel('振幅','FontSize',20)
% xlabel('时间/s','FontSize',20)

% % set( gca, 'YTick', [],'FontSize',16 );
% % set(gca,'XLim',[0 length(haar)]);
plot(1:0.005:length(haar)/200.0+1-0.005,haar,'b','linewidth',1)
% return


% plot(haar,'b')
set( gca ,'FontSize',20);
ylabel('振幅','FontSize',20)
xlabel('时间/s','FontSize',20)

% plot(haar,'Color',[1 1/2 0])
% legend('Original','DWT')



%% 极值点
feamax=[];
feamin=[];
for i=2:len-1
    flag=(haar(i)>haar(i-1))+(haar(i)>haar(i+1));
    if(flag==2)
        feamax=[feamax;[i,haar(i)]];
    elseif(flag==0)
        feamin=[feamin;[i,haar(i)]];
    end
end
% % % 画极值点
plot(feamax(:,1)/200.0+1-0.005,feamax(:,2),'y.','markersize',20)
plot(feamin(:,1)/200.0+1-0.005,feamin(:,2),'g.','markersize',20) 



%% 初始对
fea1=[];
fea2=[];
flag=1;
error=0;
thre_err=4;
threshold=4000;
num=0;
for i=1:(size(feamax,1)-(feamax(1,1)<feamin(1,1)))
    j=find(feamax(:,1)>feamin(i,1),1);
    diff=feamax(j,2)-feamin(i,2);
    error=error+1;
    if(error==thre_err+1)
        flag=1;
        error=0;
        fprintf('连续不达标-----%d----%d\n',feamax(j,1),diff);
    end
    if(flag==1 && diff<(threshold*5)...
            &&size(feamax,1)>=j+thre_err && size(feamax,1)>=i+thre_err)
        flag=2;    
        threshold=max(feamax(j:j+thre_err,2)-feamin(i:i+thre_err,2));
        fprintf('新阈值-----%d\n',threshold)
    elseif(diff>(threshold*2))
        flag=1;
        error=0;
        fprintf('差值过大-----%d----%d\n',feamax(j,1),diff);
        continue;
    elseif(diff>(threshold*3/5) && diff<(threshold*7/5))
        if(flag~=2 && ((feamax(j,2))<(fea1(num,2)-threshold/2)...
                    || (feamin(i,2))>(fea2(num,2)+threshold/2)))
            flag=1;
            error=0;
            fprintf('偏移量过大-----%d----%d\n',feamax(j,1),diff)
            continue;
        end
        threshold=0.75*threshold+0.25*diff;
        num=num+1;
        fea1(num,:)=[feamin(i,1),feamin(i,2)];
        fea2(num,:)=[feamax(j,1),feamax(j,2)];
        flag=0;
        error=0;
    end    
end
% % % 画初始对
plot(fea1(:,1)/200.0+1-0.005,fea1(:,2),'r.','markersize',30)
plot(fea2(:,1)/200.0+1-0.005,fea2(:,2),'k.','markersize',30)

hold off






%%  分段提取
xSpilit=zeros(size(fea1,1),200);
m=0;
xRange0=fea1(2,1)-fea1(1,1);
exp=4;
xm=[];
for i=1:size(fea1,1)-1
% for i=361:366
    xBase=fea1(i,1);
    yBase=fea1(i,2);
    xRange1=fea1(i+1,1)-xBase;
    if((xRange1)>(xRange0*0.7) && (xRange1)<(xRange0*1.3))
        if(i==55)
            c=1;
        end
        xRange0=xRange0*(exp-1)/exp+xRange1/exp;
        yDiff=(fea1(i,2)-fea1(i+1,2))/xRange1;
        m=m+1;
        for j=1:(xRange1-1)
            xm(length(xm)+1)=(yn(xBase+j)-yBase+yDiff*j);
            xSpilit(m,j)= xm(length(xm));
        end
    else
        xm(length(xm)+1:length(xm)+xRange1)=NaN;
    end
end


% % % 画拉直后的数据

% xm(isnan(xm))=[];
% tt=wden(xm,'modwtsqtwolog','s','mln',2,'haar');
% tt=floor(movmean(tt,5));
% tt=tt(38200:45700);
% tt=tt-min(min(tt))+1001;
% figure
% plot(tt)


figure
hold on


set( gca ,'FontSize',20);
ylabel('振幅','FontSize',20)
xlabel('时间/s','FontSize',20)

plot(1:0.005:length(xm)/200.0+1-0.005,xm,'b','linewidth',2)
% plot(xm)





%%  存储分段X数据
% xSpilit=xSpilit(1:m,:);
% xSpilit=xSpilit./(max(max(xSpilit)));
% xSpilit(57,:)=[];
% save('xSpilit.mat','xSpilit')




%%  拉直
% 
% plus=0;
% for i=1:length(fea1)-1
%     diff=(fea1(i,2)-fea1(i+1,2))/(fea1(i+1,1)-fea1(i,1));
% 
%     for j=(fea1(i,1)+1):fea1(i+1,1);
%         plus=plus+diff;
%         xn(j)=xn(j)+plus;
%     end
% end
% 
% 
% % 小波去噪
% haar = wden(xn,'modwtsqtwolog','s','mln',4,'haar');
% figure;
% hold on
% % plot(xn)
% plot(haar,'Color',[1 1/2 0])
% % legend('Original','DWT')
% 
% 
% % 提取初始特征点
% feamax=[];
% feamin=[];
% for i=2:len-1
%     flag=(haar(i)>haar(i-1))+(haar(i)>haar(i+1));
%     if(flag==2)
%         feamax=[feamax;[i,haar(i)]];
%     elseif(flag==0)
%         feamin=[feamin;[i,haar(i)]];
%     end
% end
% plot(feamax(:,1),feamax(:,2),'y.','markersize',10)
% plot(feamin(:,1),feamin(:,2),'g.','markersize',10)
% 
% 
% 
% fea1=[];
% fea2=[];
% flag=1;
% error=0;
% thre_err=4;
% threshold=4000;
% num=0;
% for i=1:(length(feamax)-(feamax(1,1)<feamin(1,1)))
%     j=find(feamax(:,1)>feamin(i,1),1);
%     diff=feamax(j,2)-feamin(i,2);
%     error=error+1;
%     if(error==thre_err+1)
%         flag=1;
%         error=0;
%         fprintf('连续不符合-----%d----%d\n',feamax(j,1),diff);
%     end
%     if(flag==1 && diff<(threshold*5)...
%             &&length(feamax)>=j+thre_err && length(feamin)>=i+thre_err)
%         flag=2;    
%         threshold=max(feamax(j:j+thre_err,2)-feamin(i:i+thre_err,2));
%         fprintf('新阈值-----%d\n',threshold)
%     elseif(diff>(threshold*2))
%         flag=1;
%         error=0;
%         fprintf('差值过大-----%d----%d\n',feamax(j,1),diff);
%         continue;
%     elseif(diff>(threshold/2) && diff<(threshold*3/2))
%         if(flag~=2 && ((feamax(j,2))<(fea1(num,2)-threshold/2)...
%                     || (feamin(i,2))>(fea2(num,2)+threshold/2)))
%             flag=1;
%             error=0;
%             fprintf('偏移量过大-----%d----%d\n',feamax(j,1),diff)
%             continue;
%         end
%         threshold=0.75*threshold+0.25*diff;
%         num=num+1;
%         fea1(num,:)=[feamin(i,1),feamin(i,2)];
%         fea2(num,:)=[feamax(j,1),feamax(j,2)];
%         flag=0;
%         error=0;
%     end    
% end
% plot(fea1(:,1),fea1(:,2),'r.','markersize',15)
% plot(fea2(:,1),fea2(:,2),'k.','markersize',15)
% hold off









