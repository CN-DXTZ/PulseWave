clc; clear;close all;

A = textread('app4.txt');
figure,plot(-A);
return

%%
num_length=405;
B = (reshape( A((size(A,2)-(floor(size(A,2)/num_length)*num_length)+1):end) ,...
              [num_length, floor(size(A,2)/num_length)]))'; %�����

 
%% ��ʼ��
% flag_isFirstWave=1; 

initThreshold=4000; % Ĭ�ϳ�ʼ��ֵ
threshold=initThreshold; % ��ֵ

flag_error=0; % 0-û��
num_error=0; % ����Դ���: <=5

preMinIndex=0; % 
nextMinIndex=0; % 

currPreprocessWave=[];

currExtremumDotList=[]; % ��ǰ��ֵ�㣺value, index, flag(0-min, 1-max)

%% ģ��ÿ��������
for ib=1:size(B,1)    
    
    % Ϊָ��ƽ���ṩ��ֵ
    if isempty(currPreprocessWave)
        currPreprocessWave(1,1)=-B(1,1);
    end
        
    currOriginWave=B(ib,:); % һ��ԭʼ����
%     figure,plot(currOriginWave);
    
    % ָ��ƽ��
    for iw=1:size(currOriginWave,2)
        currOriginDot=currOriginWave(iw);
        currPreprocessDot=currPreprocessWave(end)*(4/5)-currOriginDot*(1/5); 
        currPreprocessWave=[currPreprocessWave,currPreprocessDot];
    end
    
end

%% 
figure,plot(currPreprocessWave);