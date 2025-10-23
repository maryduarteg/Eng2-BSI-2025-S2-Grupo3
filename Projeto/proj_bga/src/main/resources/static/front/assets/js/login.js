function logar(){
    let usuario = document.getElementById("usuario").value;
    let senha = document.getElementById("senha").value;

    fetch("http://localhost:8080/apis/usuario", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            usuario: usuario,
            senha: senha
        }),
    })
        .then(response => response.json())
    .then(resp => {
        if (resp.isLogado == true){
            let usuarioResp = {
                login: usuario,
                isAtivo: resp.isAtivo,
                isLogado: resp.isLogado,
                categariaUsuarioId: resp.categariaUsuarioId,
                token: resp.token
            };
            sessionStorage.setItem("usuario", JSON.stringify(usuario));
            window.location.href = "index.html";
        }
        else
            mostrarMensagem("Usuário ou senha incorreto.", false);
    })
    .catch(error => {
        mostrarMensagem("Erro ao logar!", false);
    });
}

function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-passeio");
    if(container) {
        container.innerHTML = texto;
        container.className = 'mensagem ' + (sucesso ? 'sucesso' : 'erro');
    }
}

function cadastrarPasseio(event) {
    event.preventDefault();


    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return false;
    }

    const dados = {
        pas_data: converterDataBrasilParaISO(document.getElementById("data-passeio").value),
        pas_hora_inicio: document.getElementById("hora-inicio").value,
        pas_hora_final: document.getElementById("hora-fim").value,
        pas_chamada_feita: "N",
        pde_id: parseInt(document.getElementById("desc-passeio").value)
    };

    fetch("http://localhost:8080/apis/passeio", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams(dados),
    })
        .then(response => response.json())

        .then(res => {
            if(res.erro) {
                mostrarMensagem("Erro: " + res.erro, false);
                return;
            }
            console.log(res);
            mostrarMensagem("Sucesso! O passeio foi cadastrado.", true);
            form.reset();
            form.classList.remove("was-validated");
        })
        .catch(error => {
            console.error("Erro ao cadastrar:", error);
            mostrarMensagem("Erro ao cadastrar passeio!", false);
        });
    return false;
}