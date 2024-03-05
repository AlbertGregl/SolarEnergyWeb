/* table rendering in the fragment */
document.getElementById('dataForm').addEventListener('submit', function(event) {
    event.preventDefault();
    var year = document.getElementById('year').value;
    var month = document.getElementById('month').value;
    var url = '/mvc/energydata/byYearMonth.html?year=' + year + '&month=' + month;

    fetch(url)
        .then(response => response.text())
        .then(html => {
            var parser = new DOMParser();
            var doc = parser.parseFromString(html, 'text/html');
            var newContent = doc.body.firstChild;
            var contentLocation = document.querySelector("#energyDataTable");
            if (newContent && contentLocation) {
                contentLocation.innerHTML = newContent.innerHTML;
            } else {
                console.error('Could not find the content to replace.');
            }
        }).catch(error => {
        console.error('Error fetching the data:', error);
    });
});


/*SECTIONS; function that will show the selected section and hide the other sections*/
function showSection(sectionId) {
    console.log("Showing section:", sectionId);
    const sections = document.querySelectorAll('section');
    for (const section of sections) {
        console.log("Checking section:", section.id);
        if (section.id === sectionId) {
            console.log("Showing:", section.id);
            section.style.display = 'block';
        } else {
            console.log("Hiding:", section.id);
            section.style.display = 'none';
        }
    }
}