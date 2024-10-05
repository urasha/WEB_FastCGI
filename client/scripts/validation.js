const MESSAGES = {
    X_ERROR: "Ошибка валидации X!",
    Y_ERROR: "Ошибка валидации Y!",
    R_ERROR: "Ошибка валидации R!",
    HIT_RESULT: {
        true: "Да",
        false: "Нет"
    },
    HIT_RESULT_CLASS: {
        true: "hit-true",
        false: "hit-false"
    },
    API_URL: "/api/"
};

async function validateInput(event) {
    event.preventDefault();

    const xResult = getValidatedX(event);
    const yResult = getValidatedY(event);
    const rResult = getValidatedR(event);

    if (xResult == null || yResult == null || rResult == null) {
        return;
    }

    const requestData = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            x: xResult,
            y: yResult,
            r: rResult
        })
    };

    fetch(MESSAGES.API_URL, requestData)
        .then(response => response.json())
        .then(data => {
            addDataRow(xResult, yResult, rResult, data["isHit"], data["time"]);
        });
}

function addDataRow(x, y, r, hit, time) {
    let tableBody = document.querySelector("#data-table tbody");
    let noDataRow = document.getElementById("no-data");

    if (noDataRow) {
        noDataRow.remove();
    }

    let newRow = document.createElement("tr");

    newRow.innerHTML = `
        <td>${x}</td>
        <td>${y}</td>
        <td>${r}</td>
        <td>${new Date().toLocaleString()}</td>
        <td>${time} секунд</td>
        <td class="${MESSAGES.HIT_RESULT_CLASS[hit]}">${MESSAGES.HIT_RESULT[hit]}</td>
    `;

    tableBody.appendChild(newRow);
}

function getValidatedX() {
    const possibleXValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];

    let xInput = document.querySelector('input[name="x-value"]:checked');
    let x = xInput == null ? NaN : parseInt(xInput.value);

    let validationResult = !isNaN(x) && possibleXValues.includes(x);
    document.querySelector("#x-error").textContent = validationResult ? "" : MESSAGES.X_ERROR;

    return validationResult ? x : null;
}

function getValidatedY() {
    let yInput = document.querySelector('#y-value');
    let y = parseFloat(yInput.value);

    let validationResult = !isNaN(y) && y <= 5 && y >= -5;
    document.querySelector("#y-error").textContent = validationResult ? "" : MESSAGES.Y_ERROR;

    return validationResult ? y : null;
}

function getValidatedR() {
    const possibleRValues = [1, 1.5, 2, 2.5, 3];

    let rInput = document.querySelector('#r-selection');
    let r = parseInt(rInput.value);

    let validationResult = !isNaN(r) && possibleRValues.includes(r);
    document.querySelector("#r-error").textContent = validationResult ? "" : MESSAGES.R_ERROR;

    return validationResult ? r : null;
}

let submitButton = document.querySelector("#submit-button");
submitButton.addEventListener("click", (event) => validateInput(event));
