clc; clear;close all;
load('xOrigin.mat')

xDiff1=xOrigin(:,2:200)-xOrigin(:,1:199);
xDiff2=xDiff1(:,2:199)-xDiff1(:,1:198);


threshold=[1,41, 237];
len=length(xOrigin);
for i=1:len
    if(find(threshold==i))
        figure
    end
    hold on
    plot(xOrigin(i,:))
end
for i=1:len-1
    if(find(threshold==i))
        figure
    end
    hold on
    plot(xDiff1(i,:))
end
for i=1:len-2
    if(find(threshold==i))
        figure
    end
    hold on
    plot(xDiff2(i,:))
end