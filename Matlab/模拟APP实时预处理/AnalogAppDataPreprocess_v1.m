clc; clear;close all;

% 数据1
% A = textread('app4.txt');


% 数据2
db = database('wave', 'root', '1234',...
    'com.mysql.jdbc.Driver', 'jdbc:mysql://localhost:3306/wave');
curs = exec(db, 'select * from data1');
curs = fetch(curs)  ;
ress = curs.Data;  
ress = cell2mat(ress);  
A = -ress(:,2); % 4100:95527

%%
num_length=405;
len=length(A);
B = (reshape( A((len-(floor(len/num_length)*num_length)+1):end) ,...
              [num_length, floor(len/num_length)]))'; %　拆分

 

%% 模拟每次来数据
% 初始化
currPreprocessWave=[];
currLimDotList=[]; % 当前极值点：value, index, flag_lim(0-min, 2-max)

for ib=1:size(B,1)    
    
    % 为指数平滑提供初值
    if isempty(currPreprocessWave)
        currPreprocessWave(1,1)=-B(1,1);
    end
        
    currOriginWave=B(ib,:); % 一组原始波形
%     figure,plot(currOriginWave);
    
    % 指数平滑
    for io=1:length(currOriginWave)
        currOriginDot=currOriginWave(io);
        currPreprocessDot=currPreprocessWave(end)*(4/5)-currOriginDot*(1/5); 
        currPreprocessWave=[currPreprocessWave,currPreprocessDot];
    end
    
    
    
    % 极值点
    % 如果不存在第一个波
    if isempty(currLimDotList)
        start=2;
    else
        start=currLimDotList(end,2)+1;
    end
    
    for i=start:(length(currPreprocessWave)-1)
        flag=(currPreprocessWave(i)>=currPreprocessWave(i-1))...
            + (currPreprocessWave(i)>=currPreprocessWave(i+1)); % 2-max,0-min
        % 极值
        if flag~=1
            currLimDotList=[currLimDotList;...
                currPreprocessWave(i), i, flag];
        end
    end   
end

    %% 极值点去伪存真
    i=2;
    while i<=size(currLimDotList,1)
        % 同向极值
        if(currLimDotList(i,3)==currLimDotList(i-1,3))
            
            % (i)比(i-1)更极限
            if ~xor(currLimDotList(i,1)>currLimDotList(i-1,1),...
                    currLimDotList(i,3)>0)
                currLimDotList(i-1,:)=[];
            else
                currLimDotList(i,:)=[];
            end
        % 反向抖动
        elseif (currLimDotList(i,2)-currLimDotList(i-1,2))<=10
                currLimDotList(i,:)=[];
        else
            i=i+1;
        end
    end  

    

%% 提取波形，基线漂移矫正
% 初始化
initThreshold=5000; % 默认初始阈值
threshold=initThreshold; % 阈值

flag_error=0; % 0-没错，
num_error=0; % 错误对次数: <6

WaveList=[];

% 令起始值为最小点
if currLimDotList(1,3)==2
    currLimDotList(1,:)=[];
end
   

i=1;
while i<size(currLimDotList,1)

end  



%%
figure,plot(currPreprocessWave);
hold on;
plot(currLimDotList(:,2),currLimDotList(:,1),'r.','markersize',15);

% aa=currLimDotList(:,2);
% bb=ones(size(aa))*-1300000;
% hold on
% stem(aa,bb)

