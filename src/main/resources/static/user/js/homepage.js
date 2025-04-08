
    document.addEventListener("DOMContentLoaded", function () {
    const roundTripRadio = document.getElementById("round-trip");
    const oneWayRadio = document.getElementById("one-way");
    const oneWaySection = document.querySelector(".destination-one-way");
    const roundTripSection = document.querySelector(".destination-round-trip");

    roundTripRadio.addEventListener("click", function () {
    oneWaySection.classList.remove("d-block");
    oneWaySection.classList.add("d-none");
    roundTripSection.classList.remove("d-none");

});

    oneWayRadio.addEventListener("click", function () {
    oneWaySection.classList.remove("d-none");
    oneWaySection.classList.add("d-block");
    roundTripSection.classList.add("d-none");
});

});
    function changeCount(event, type, value) {
    event.stopPropagation();
    event.preventDefault();
    let element = document.getElementById(type);
    let count = parseInt(element.textContent) + value;
    if (count < 0) count = 0;
    element.textContent = count;
}

    function submitForm(e) {
    e.preventDefault();
    let counts = {
    adult: parseInt(document.getElementById('adult').textContent),
    child: parseInt(document.getElementById('child').textContent),
    infant: parseInt(document.getElementById('infant').textContent)
};

    let passengerText = [];
    if (counts.adult > 0) passengerText.push(`${counts.adult} Người lớn`);
    if (counts.child > 0) passengerText.push(`${counts.child} Trẻ em`);
    if (counts.infant > 0) passengerText.push(`${counts.infant} Em bé`);

    document.getElementById('passengerInput').value = passengerText.join(", ") || "Chọn số hành khách";
    let dropdown = bootstrap.Dropdown.getInstance(document.getElementById('passengerInput'));
    if (dropdown) dropdown.hide();
}