
let searchFormData = {
    departureAirport: null,
    arrivalAirport: null,
    departureTime: null,
    arrivalTime: null,
    arrivalOneWay:"",
    isOneWay: false,
    isRoundTrip:false,
    quantity:{
        adult: 1,
        child: 0,
        infant: 0
    },
};

document.addEventListener("DOMContentLoaded", function () {
    const roundTripRadio = document.getElementById("round-trip");
    const oneWayRadio = document.getElementById("one-way");
    const oneWaySection = document.querySelector(".destination-one-way");
    const roundTripSection = document.querySelector(".destination-round-trip");
    // const input = document.getElementById("arrival")
    roundTripRadio.addEventListener("click", function () {
    oneWaySection.classList.remove("d-block");
    oneWaySection.classList.add("d-none");
    roundTripSection.classList.remove("d-none");

});

    oneWayRadio.addEventListener("click", function () {
    oneWaySection.classList.remove("d-none");
    // oneWaySection.classList.add("d-block");
    roundTripSection.classList.add("d-none");
    // input.id = "destination-one-way"

});

});


    window.addEventListener("load", (event) => {
        document.getElementById('passengers').value = "1 Người lớn"
        sessionStorage.clear()

    });

    function changeCount(event, type, value) {
    event.stopPropagation();
    event.preventDefault();
    let element = document.getElementById(type);
    let count = parseInt(element.textContent) + value;
    if(type === "adults" && count === 0 && value === -1){
        return;
    }
    if (count < 0) count = 0;
    element.textContent = count;
}

    function submitForm(e) {
    e.preventDefault();
    let counts = {
    adult: parseInt(document.getElementById('adults').textContent),
    child: parseInt(document.getElementById('child').textContent),
    infant: parseInt(document.getElementById('infant').textContent)
};


    // searchFormData.quantity.adult = counts.adult
    // searchFormData.quantity.child = counts.child
    // searchFormData.quantity.infant = counts.infant


    let passengerText = [];
    if (counts.adult > 0) passengerText.push(`${counts.adult} Người lớn`);
    if (counts.child > 0) passengerText.push(`${counts.child} Trẻ em`);
    if (counts.infant > 0) passengerText.push(`${counts.infant} Em bé`);

    document.getElementById('passengers').value = passengerText.join(", ") || "Chọn số hành khách";
    let dropdown = bootstrap.Dropdown.getInstance(document.getElementById('passengers'));
    if (dropdown) dropdown.hide();
}

    function submitFormSearch(e){
        event.preventDefault();

                const checkedRoundTrip = document.getElementById("round-trip")
                searchFormData.isRoundTrip= checkedRoundTrip.checked

                const checkedOneWay = document.getElementById("one-way")
                searchFormData.isOneWay= checkedOneWay.checked

                const inputDeparture = document.getElementById("departure")
                searchFormData.departureAirport = inputDeparture.value

                if(searchFormData.isRoundTrip === true){
                        const inputArrival = document.getElementById("arrival")
                        searchFormData.arrivalAirport = inputArrival.value
                    }
                if(  searchFormData.isOneWay === true){
                        const inputArrivalOneWay = document.getElementById("destination-one-way")
                        searchFormData.arrivalOneWay = inputArrivalOneWay.value
                    }

                const inputDepartureDate = document.getElementById("departure-date")
                searchFormData.departureTime = inputDepartureDate.value

                const inputArrivalDate = document.getElementById("arrival-date")
                searchFormData.arrivalTime = inputArrivalDate.value

                const inputAdults = document.getElementById("adults");
                searchFormData.quantity.adult = parseInt(inputAdults.textContent)

               const inputChild = document.getElementById("child");
                searchFormData.quantity.child = parseInt(inputChild.textContent)

                const inputInfant = document.getElementById("infant");
                searchFormData.quantity.infant = parseInt(inputInfant.textContent)


                sessionStorage.setItem("data",JSON.stringify(searchFormData));

                document.getElementById("searchForm").submit();

    }

