clc; clear;close all;

% ����1
% A = textread('app4.txt');


% ����2
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
              [num_length, floor(len/num_length)]))'; %�����

 

%% ģ��ÿ��������
% ��ʼ��
currPreprocessWave=[];
currLimDotList=[]; % ��ǰ��ֵ�㣺value, index, flag_lim(0-min, 2-max)

for ib=1:size(B,1)    
    
    % Ϊָ��ƽ���ṩ��ֵ
    if isempty(currPreprocessWave)
        currPreprocessWave(1,1)=-B(1,1);
    end
        
    currOriginWave=B(ib,:); % һ��ԭʼ����
%     figure,plot(currOriginWave);
    
    % ָ��ƽ��
    for io=1:length(currOriginWave)
        currOriginDot=currOriginWave(io);
        currPreprocessDot=currPreprocessWave(end)*(4/5)-currOriginDot*(1/5); 
        currPreprocessWave=[currPreprocessWave,currPreprocessDot];
    end
    
    
    
    % ��ֵ��
    % ��������ڵ�һ����
    if isempty(currLimDotList)
        start=2;
    else
        start=currLimDotList(end,2)+1;
    end
    
    for i=start:(length(currPreprocessWave)-1)
        flag=(currPreprocessWave(i)>=currPreprocessWave(i-1))...
            + (currPreprocessWave(i)>=currPreprocessWave(i+1)); % 2-max,0-min
        % ��ֵ
        if flag~=1
            currLimDotList=[currLimDotList;...
                currPreprocessWave(i), i, flag];
        end
    end   
end

    %% ��ֵ��ȥα����
    i=2;
    while i<=size(currLimDotList,1)
        % ͬ��ֵ
        if(currLimDotList(i,3)==currLimDotList(i-1,3))
            
            % (i)��(i-1)������
            if ~xor(currLimDotList(i,1)>currLimDotList(i-1,1),...
                    currLimDotList(i,3)>0)
                currLimDotList(i-1,:)=[];
            else
                currLimDotList(i,:)=[];
            end
        % ���򶶶�
        elseif (currLimDotList(i,2)-currLimDotList(i-1,2))<=10
                currLimDotList(i,:)=[];
        else
            i=i+1;
        end
    end  

    

%% ��ȡ���Σ�����Ư�ƽ���
% ��ʼ��
initThreshold=5000; % Ĭ�ϳ�ʼ��ֵ
threshold=initThreshold; % ��ֵ

flag_error=0; % 0-û��
num_error=0; % ����Դ���: <6

WaveList=[];

% ����ʼֵΪ��С��
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

