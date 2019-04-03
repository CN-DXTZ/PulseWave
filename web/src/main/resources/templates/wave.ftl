<!DOCTYPE html>
<html>
<head>
    <title>${information}</title>
    <style>
        .table_left {
            float: left;
            width: 25%;
        }

        .table_right {
            float: left;
            width: 50%;
        }
    </style>
</head>
<body>

${information}<br/><br/>

<div class="table_left">时间</div>
<div class="table_right">脉搏值</div>
<br/>

<span>
    <#list waveList as wave>
        <div class="table_left">${wave.time}</div>
        <div class="table_right">${wave.value}</div><br/>
    </#list>
</span>

<button onclick="location.href='manager'">返回管理</button>

</body>
</html>