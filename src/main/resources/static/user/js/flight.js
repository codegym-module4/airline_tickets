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


    const storage = JSON.parse(window.sessionStorage.getItem("data"))
    if (storage.isRoundTrip === true) {
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

    if (storage.isOneWay === true) {
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
    $("#adults").text(storage.quantity.adult)
    $("#child").text(storage.quantity.child)
    $("#infant").text(storage.quantity.infant)

    document.getElementById('passengers').value = `${storage.quantity.adult} Người lớn, ${storage.quantity.child} Trẻ em, ${storage.quantity.infant} Em bé`

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
        let condition = generateCompareURL();
        let flight_type = $("input[name='flight_type']").val();

        if (flight_type === "ONEWAY" && condition.selectedIds.length > 1) {
            $("#flightDetailsModal").modal("hide");
            $("#notifyErrorOneWay").modal("show");
            $("#selecDetailModal").modal("hide");
            return
        }
        if (flight_type === "ROUND-TRIP" && condition.selectedIds.length > 2) {
            $("#flightDetailsModal").modal("hide");
            $("#notifyErrorRoundTrip").modal("show");
            $("#selecDetailModal").modal("hide");
            return
        }

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
            if (jqXhr.responseJSON.message) {
                $("#flightDetailsModal").modal("hide");
                $("#detailModal .modal-body").html((jqXhr.responseJSON.message));
                $("#detailModal").modal("show");
            }

        });

    });

    $(document).on("click", "#btnSelectCompare", function () {
        let data = generateCompareURL();

        if (data.selectedIds.length <= 1) {
            $("#notifyErrorCompare").modal("show");
            return
        }

        $.ajax({
            type: 'GET',
            url: data.url,
            dataType: 'json'
        }).done(function (data) {
            if (data.html) {
                $("#selecDetailModal .modal-body").html(data.html);
                $("#selecDetailModal").modal("show");

            }
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                alert(jqXhr.responseJSON.message);
            }
        });
    });


    $(document).on("click", "#btnConfirmCompare", function () {

        const condition = JSON.parse(sessionStorage.getItem("checkedFlight"));
        let flight_type = $("input[name='flight_type']").val();

        if (flight_type === "ONEWAY" && condition.selectedIds.length > 1) {
            $("#flightDetailsModal").modal("hide");
            $("#notifyErrorOneWay").modal("show");
            $("#selecDetailModal").modal("hide");
            return
        }

        if (flight_type === "ROUND-TRIP" && condition.selectedIds.length > 2) {
            $("#flightDetailsModal").modal("hide");
            $("#notifyErrorRoundTrip").modal("show");
            $("#selecDetailModal").modal("hide");
            return
        }

        let data = getDataRequired();

        $.ajax({
            type: 'GET',
            url: '/api/flight/confirm',
            data: data,
            dataType: 'json'
        }).done(function (data) {
            if (data.html) {
                $("#selecDetailModal").modal("hide");
                $("#flightDetailsModal .modal-body").html(data.html);
                $("#flightDetailsModal").modal("show");
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
        num_of_adult: num_of_adult,
        num_of_child: num_of_child,
        num_of_baby: num_of_baby,
        flight_type: flight_type,
        idDepart: idDepart,
        idArrival: idArrival,
    }

    return object;

}

function generateCompareURL() {
    let object
    let selectedDepart = $(".radio-depart:checked").map(function () {
        return $(this).data("id");
    }).get();
    let selectedArrival = $(".radio-arrival:checked").map(function () {
        return $(this).data("id");
    }).get();
    let selectedIds = [...selectedDepart, ...selectedArrival];
    console.log("selectedIds", selectedIds);
    let url = "/api/flight/compare/" + selectedIds.join(",");
    object = {
        selectedIds: selectedIds,
        url: url
    };
    sessionStorage.setItem("checkedFlight", JSON.stringify(object));
    return object;
}

$(document).ready(function () {
    $("input[type='checkbox']").on("change", function () {
        generateCompareURL();
    });
});

$('#selecDetailModal').on('hidden.bs.modal', function () {
    $("input[type='checkbox']").prop("checked", false);
    sessionStorage.removeItem("checkedFlight");
});








