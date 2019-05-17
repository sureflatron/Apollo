function getElementsByClassName(className, context) {
	if (context.getElementsByClassName)
		return context.getElementsByClassName(className);
	var elems = document.querySelectorAll ? context.querySelectorAll("." + className) : (function() {
		var all = context.getElementsByTagName("*"), elements = [], i = 0;
		for (; i < all.length; i++) {
			if (all[i].className && (" " + all[i].className + " ").indexOf(" " + className + " ") > -1 && indexOf.call(elements, all[i]) === -1)
				elements.push(all[i]);
		}
		return elements;
	})();
	return elems;
};

function sort(ascending, columnClassName, tableId) {
	var tbody = document.getElementById(tableId).getElementsByTagName("tbody")[0];
	var rows = tbody.getElementsByTagName("tr");

	var unsorted = true;

	while (unsorted) {
		unsorted = false

		for (var r = 0; r < rows.length - 1; r++) {
			var row = rows[r];
			var nextRow = rows[r + 1];

			// var value = row.getElementsByClassName(columnClassName)[0].innerHTML;
			// var nextValue = nextRow.getElementsByClassName(columnClassName)[0].innerHTML;
			var value = getElementsByClassName(columnClassName, row)[0].innerHTML;
			var nextValue = getElementsByClassName(columnClassName, nextRow)[0].innerHTML;

			// console.log('value:' + value);
			// console.log('nextValue: ' + nextValue);

			value = value.replace(',', ''); // in case a comma is used in float number
			// value = value.replace('.', ''); // in case a comma is used in float number
			nextValue = nextValue.replace(',', '');

			// var valueFloat = parseFloat(value);
			// var nextValueFloat = parseFloat(nextValue);

			if (!isNaN(value)) {
				value = parseFloat(value);
				nextValue = parseFloat(nextValue);
			}

			// console.log('value after: ' + value);

			if (ascending ? value > nextValue : value < nextValue) {
				tbody.insertBefore(nextRow, row);
				unsorted = true;
			}
		}
	}
};
