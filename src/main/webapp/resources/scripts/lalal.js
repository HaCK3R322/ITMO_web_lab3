// function okay() {
//     let coordsDiv = document.getElementById("coordsDiv");
//     alert("page loaded");
//     coordsDiv.addEventListener("click", event => {
//         let r = document.getElementById("form:r-inputtext").value;
//         let coordsDiv = document.getElementById("coordsDiv");
//
//         if(r > 0) {
//             let rect = coordsDiv.getBoundingClientRect();
//
//             alert(rect.)
//         } else {
//             alert("R must be greater than zero!");
//         }
//     })
// }

// document.getElementById('coordsDiv').onclick = function clickEvent(e) {
//     // e = Mouse click event.
//     var rect = e.target.getBoundingClientRect();
//     var x = e.clientX - rect.left; //x position within the element.
//     var y = e.clientY - rect.top;  //y position within the element.
//     alert("Left? : " + x + " ; Top? : " + y + ".");
// }

function lololo() {
    document.getElementById('coordsDiv').onclick = function clickEvent(e) {
        let r = document.getElementById("form:r-inputtext").value;
        if(r > 0) {
            let rect = e.target.getBoundingClientRect();
            let x = e.clientX - rect.left;
            let y = e.clientY - rect.top;

            x = x / 500;
            y = y / 500;

            x = 2.5 * (x - 0.5);
            y = -2.5 * (y - 0.5);

            x *= r;
            y *= r;

            alert("Left? : " + x + " ; Top? : " + y + ".");
        } else {
            alert("R must be grater than zero!");
        }
    }
}
setTimeout(lololo, 100);

// alert("in getpos f 1/4");-->
// <!--                let rect = e.getBoundingClientRect();-->
// <!--                alert("in getpos f 2/4");-->
// <!--                let x = e.clientX - rect.left;-->
// <!--                alert("in getpos f 3/4");-->
// <!--                let y = e.clientY - rect.top;-->
// <!--                alert("in getpos f 4/4");-->
// <!--                return {-->
// <!--                    x,-->
// <!--                    y-->
// <!--                }-->
//

// setTimeout(okay, 1000);