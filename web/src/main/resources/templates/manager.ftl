<!DOCTYPE html>
<html>
<head>
    <title>${information}</title>
</head>
<body>

${information}<br/><br/>

<span>
    <#list managerList as item>
        <button onclick="location.href='${item.target_url}'">${item.target}</button><br/>
    </#list>
</span>


</body>
</html>