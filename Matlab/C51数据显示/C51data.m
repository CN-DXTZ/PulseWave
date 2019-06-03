clc; clear;close all;
fid = fopen('C51.bin', 'r');
[A,n]=fread(fid);
[max,~]=size(A);
max=max-43;
D=zeros((floor(max/43)+1)*10,1);

i=2;l=1;
while i<max
    for j=1:10
        n=0;
        for k=1:3
            n=n*256+A(i);
            i=i+1;
        end
        D(l)=n;
        l=l+1;
    end
    i=i+13;
end

plot(-D)