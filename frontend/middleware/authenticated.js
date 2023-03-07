export default function({ redirect }) {
  // the usuario from the local storage
  let usuario = JSON.parse(window.localStorage.getItem("usuario"));

  // If the user is not authenticated
  if (usuario === null) {
    console.log("No hay usuario en el local storage");
    return redirect("/usuarios");
  }
}
