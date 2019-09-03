// show a single table
function showTable(tableId, jsonArray, divParent) {
	// create wrapping div to allow horizontal slide
	var div = document.createElement('div');
	div.className = "table-wrapper";
	// add table
	var tableId = 'table_' + tableId;
	var table = document.createElement('table');
	table.id = tableId;
	table.className = 'display'; // 'default';
	table.style.width = "100%";
	div.appendChild(table);
	// find child keys
	var columns = []
	var tr = document.createElement('tr');
	for (var key in jsonArray[0]) {
		// add child keys as column name
		var th = document.createElement('th');
		th.innerHTML = key;
		tr.appendChild(th);
		// add child keys to array
		columns.push({"data": key})
	}
	var thead = document.createElement('thead');
	thead.appendChild(tr);
	var tfoot = document.createElement('tfoot');
	tfoot.appendChild(tr.cloneNode(true));
	table.appendChild(thead);
	table.appendChild(tfoot);
	// show table
	var maxDisplayLength = 10;
	divParent.appendChild(div);
	$('#' + tableId).DataTable( {
        "data": jsonArray,
        "columns": columns,
        "ordering": false,
        "searching": false,
        "info": false,
        "paging": jsonArray.length > maxDisplayLength,
        "bLengthChange": false,
        "pageLength": maxDisplayLength
    } );
	// show info
	var infoStr = "display " + Math.min(jsonArray.length, maxDisplayLength) + " out of " + jsonArray.length + " entries";
	showTitle(infoStr, 'p', divParent);
}

function showTitle(title, level, divParent) {
	// add title
	var h = document.createElement(level);
	h.innerHTML = title;
	divParent.appendChild(h);
}

// show multiple tables in search page
function showSearchTables(json) {
	// parse json
	var jsonObj = JSON.parse(json);
	var div = document.getElementById('div_id');
	// show labels
	for (var label in jsonObj) {
		showTitle(label, 'h2', div)
		// show properties under this lable
		var propJson = jsonObj[label];
		for (var prop in propJson) {
			showTitle(prop, 'h3', div);
			showDownloadLink(label + prop + '.json', propJson[prop], div);
			showTable('search_' + label + '_' + prop, propJson[prop], div);
		}
	}
}

// show multiple tables in statistic page
function showStatisticsTables(json) {
	// parse json
	var jsonObj = JSON.parse(json);
	var div = document.getElementById('div_id');
	// show titles
	for (var title in jsonObj) {
		showTitle(title, 'h3', div);
		// show content as table
		showTable('statistic_' + title, jsonObj[title], div);
	}
}

function showDownloadLink(filename, content, div) {
	var d = document.createElement('a');
	d.innerHTML = 'download';
	d.addEventListener('click', function() {
		// create an invisible download link, click it, and delete it
		var a = document.createElement('a');
		a.setAttribute('href', 'data:text/plain,' + JSON.stringify(content));
	    a.setAttribute('download', filename);
	    a.style.display = 'none';
	    document.body.appendChild(a);
	    a.click();
	    document.body.removeChild(a);
	});
    div.appendChild(d);
}