/* toasts */
if (document.querySelector('.toast.show')) {
    var toastEl = document.getElementById('successToast');
    var toast = new bootstrap.Toast(toastEl);
    toast.show();
}


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

document.getElementById('weatherForm').addEventListener('submit', function(event) {
    event.preventDefault();
    var city = document.getElementById('city').value;
    var url = '/mvc/weather/temperature.html?city=' + city;

    fetch(url)
        .then(response => response.text())
        .then(html => {
            var parser = new DOMParser();
            var doc = parser.parseFromString(html, 'text/html');
            var newContent = doc.body.firstChild;
            var contentLocation = document.querySelector("#temperatureResult");
            if (newContent && contentLocation) {
                contentLocation.innerHTML = newContent.innerHTML;
                var modalElement = document.getElementById('getTemperatureModal');
                var modalInstance = bootstrap.Modal.getInstance(modalElement);
                if (modalInstance) {
                    modalInstance.hide();
                }
            } else {
                console.error('Could not find the content to replace.');
            }
        }).catch(error => {
        console.error('Error fetching the data:', error);
    });
});


/*SECTIONS; function that will show the selected section and hide the other sections*/
function showSection(sectionId) {

    if (sectionId === 'energy')
    {
        energyDataModal.style.display = 'block';
    } else
    {
        energyDataModal.style.display = 'none';
    }

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