(function () {
    'use strict';
        let dataDepature = null;
        let dataArrival = null;


        const tableDeparture = document.querySelectorAll('#table1 input[type="radio"]');
        tableDeparture.forEach((radio) => {
            radio.addEventListener("change", (event) => {
                if (event.target.checked) {
                    dataDepature = event.target.value;
                }
            });
        });


        const tableArrival = document.querySelectorAll('#table2 input[type="radio"]');
        tableArrival.forEach((radio) => {
            radio.addEventListener("change", (event) => {
                if (event.target.checked) {
                    dataArrival = event.target.value;

                }
            });
        });

        document.getElementById("btnConfirm").addEventListener("click", () => {
            const modalData = document.getElementById("modalData");

            modalData.innerHTML = `
                <p>Bảng 1: ${dataDepature || "Chưa chọn"}</p>
                <p>Bảng 2: ${dataArrival || "Chưa chọn"}</p>
            `;
        });

        const storage = JSON.parse(window.sessionStorage.getItem("data"))
        if(storage.isRoundTrip === true){
            const divArrivalOneWay = document.getElementById("arrival-option")
            divArrivalOneWay.classList.add("d-block")

            const inputDeparture = document.getElementById('departure');
            inputDeparture.value = storage.departureAirport

            const inputArrival = document.getElementById('arrival');
            inputArrival.value = storage.arrivalAirport

            const inputDepartureDate = document.getElementById('departure-date');
            inputDepartureDate.value = storage.departureTime

            const inputArrivalDate = document.getElementById("arrival-date");
            inputArrivalDate.value = storage.arrivalTime

            const inputAdults = document.getElementById("adults");
            inputAdults.textContent = storage.quantity.adult

            const inputChild = document.getElementById("child");
            inputChild.textContent = storage.quantity.child

            const inputInfant = document.getElementById("infant");
            inputInfant.textContent = storage.quantity.infant


            // sessionStorage.clear();

        }
        if(storage.isOneWay === true) {
            const divArrivalOneWay = document.getElementById("arrival-option")
            divArrivalOneWay.classList.add("d-none")

            const inputDeparture = document.getElementById('departure');
            inputDeparture.value = storage.departureAirport

            const inputArrivalOneWay = document.getElementById('arrival');
            inputArrivalOneWay.value = storage.arrivalOneWay

            const inputDepartureDate = document.getElementById('departure-date');
            inputDepartureDate.value = storage.departureTime

            const inputAdult = document.getElementById("adults");
            inputAdult.textContent = storage.quantity.adult

            const inputChild = document.getElementById("child");
            inputChild.textContent = storage.quantity.child

            const inputInfant = document.getElementById("infant");
            inputInfant.textContent = storage.quantity.infant
            // sessionStorage.clear();
        }

        $(document).on("click", ".btn-data-flight", function () {
            let id = $(this).data("id");
            $.ajax({
                type: 'GET',
                url: '/api/flight/' + id,
                dataType: 'json'
            }).done(function (data) {
                if (data.html) {
                    $("#detailModal .modal-body").html(data.html);
                    $("#detailModal").modal("show");
                }
            }).fail(function (jqXhr, json, errorThrown) {
                if (jqXhr.responseJSON.errors) {
                    alert(jqXhr.responseJSON.message);
                }
            });
        });
})();





