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

        // document.getElementById("btnConfirm").addEventListener("click", () => {
        //     const modalData = document.getElementById("modalData");
        //
        //     modalData.innerHTML = `
        //         <p>Bảng 1: ${dataDepature || "Chưa chọn"}</p>
        //         <p>Bảng 2: ${dataArrival || "Chưa chọn"}</p>
        //     `;
        // });

        const storage = JSON.parse(window.sessionStorage.getItem("data"))
        if(storage.isRoundTrip === true){
            const divArrivalOneWay = document.getElementById("arrival-option")
            divArrivalOneWay.classList.add("d-block")

            const div = document.querySelector(".destination-one-way")
            div.classList.add("d-none")

            const inputDeparture = document.getElementById('departure');
            inputDeparture.value = storage.departureAirport

            const inputArrival = document.getElementById('arrival');
            inputArrival.value = storage.arrivalAirport

            const inputDepartureDate = document.getElementById('departure-date');
            inputDepartureDate.value = storage.departureTime

            const inputArrivalDate = document.getElementById("arrival-date");
            inputArrivalDate.value = storage.arrivalTime

            // sessionStorage.clear();
        }

        if(storage.isOneWay === true) {
            const divArrivalOneWay = document.getElementById("arrival-option")
            divArrivalOneWay.classList.add("d-none")

            const div = document.querySelector(".destination-one-way")
            div.classList.add("d-block")

            const divrt = document.querySelector(".destination-round-trip")
            divrt.classList.add("d-none")

            const inputDeparture = document.getElementById('departure');
            inputDeparture.value = storage.departureAirport

            const inputArrivalOneWay = document.querySelector('.des-one-way');
            inputArrivalOneWay.value = storage.arrivalOneWay

            const inputDepartureDate = document.getElementById('departure-date');
            inputDepartureDate.value = storage.departureTime

            // sessionStorage.clear();
        }

        const inputAdults = document.getElementById("adults");
        inputAdults.textContent = storage.quantity.adult

        const inputChild = document.getElementById("child");
        inputChild.textContent = storage.quantity.child

        const inputInfant = document.getElementById("infant");
        inputInfant.textContent = storage.quantity.infant

        $("input[name='num_of_adult']").val(storage.quantity.adult);
        $("input[name='num_of_child']").val(storage.quantity.child);
        $("input[name='num_of_baby']").val(storage.quantity.infant);

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

    $(document).on("click", "#btnConfirm", function () {
        let data = getDataRequired();
        $.ajax({
            type: 'GET',
            url: '/api/flight/confirm',
            data: data,
            dataType: 'json'
        }).done(function (data) {
            if (data.html) {
                $("#flightDetailsModal .modal-body").html(data.html);
                $("#flightDetailsModal").modal("show");
            }
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                alert(jqXhr.responseJSON.message);
            }
        });
    });
    $(document).on("click", "#btnBooking", function () {
        let data = getDataRequired();
        let total = $("input[name='total']").val();
        data['total'] = total;
        $.ajax({
            type: 'POST',
            url: '/api/flight/accept-booking',
            data: data,
            dataType: 'json'
        }).done(function (data) {
            if (data.url) {
                window.location.href = data.url
            }
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                alert(jqXhr.responseJSON.message);
            }
        });
    });
})();

function getDataRequired() {
    let object;
    let num_of_adult = $("input[name='num_of_adult']").val();
    let num_of_child = $("input[name='num_of_child']").val();
    let num_of_baby = $("input[name='num_of_baby']").val();
    let flight_type = $("input[name='flight_type']").val();
    let idDepart = $(".radio-depart:checked").val();
    let idArrival = null;
    if (flight_type == "ROUND-TRIP") {
        idArrival = $(".radio-arrival:checked").val();
    }
    object = {
        num_of_adult : num_of_adult,
        num_of_child : num_of_child,
        num_of_baby : num_of_baby,
        flight_type : flight_type,
        idDepart : idDepart,
        idArrival : idArrival
    }

    return object;
}





