import {setupTabsNavigation} from "./app.js";

setupTabsNavigation(document.querySelector("div"), 3);
window.addEventListener("submit", e => e.preventDefault());
document.getElementById("save").onclick = () => {
    //save
};
document.getElementById("resetPassword").onclick = () => {
    //reset password
};
document.getElementById("logout").onclick = () => {
    //logout
};