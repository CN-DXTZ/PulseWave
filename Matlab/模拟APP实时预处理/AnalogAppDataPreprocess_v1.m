clc; clear;close all;

A = textread('app4.txt');
figure,plot(-A);
return

%%
num_length=405;
B = (reshape( A((size(A,2)-(floor(size(A,2)/num_length)*num_length)+1):end) ,...
              [num_length, floor(size(A,2)/num_length)]))'; %　拆分

 
%% 初始化
% flag_isFirstWave=1; 

initThreshold=4000; % 默认初始阈值
threshold=initThreshold; % 阈值

flag_error=0; % 0-没错，
num_error=0; % 错误对次数: <=5

preMinIndex=0; % 
nextMinIndex=0; % 

currPreprocessWave=[];

currExtremumDotList=[]; % 当前极值点：value, index, flag(0-min, 1-max)

%% 模拟每次来数据
for ib=1:size(B,1)    
    
    % 为指数平滑提供初值
    if isempty(currPreprocessWave)
        currPreprocessWave(1,1)=-B(1,1);
    end
        
    currOriginWave=B(ib,:); % 一组原始波形
%     figure,plot(currOriginWave);
    
    % 指数平滑
    for iw=1:size(currOriginWave,2)
        currOriginDot=currOriginWave(iw);
        currPreprocessDot=currPreprocessWave(end)*(4/5)-currOriginDot*(1/5); 
        currPreprocessWave=[currPreprocessWave,currPreprocessDot];
    end
    
end

%% 
figure,plot(currPreprocessWave);