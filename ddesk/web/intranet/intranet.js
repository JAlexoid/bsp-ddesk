
function selectedProject(element){
    ProjectAccessor.moduleList(element.options[element.selectedIndex].value, setModules);
}

function setSelectedValue(item, hidden){
    document.getElementById(hidden).value = item.options[item.selectedIndex].value;
}

function setModules(data){
       var a = document.getElementById("moduleSelect").options;
       while(a.length > 0){
            a[0] = null;
       }
       a[0] = new Option("","", false);
       var i = 1;
       for( var amen in data){
        var module = data[amen];
        a[i] = new Option(module.module,module.id, false);
        i++;
       }
}