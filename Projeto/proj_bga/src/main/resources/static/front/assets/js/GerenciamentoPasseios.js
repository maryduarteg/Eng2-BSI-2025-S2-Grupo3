document.addEventListener('DOMContentLoaded', (event) => {
    const btnMostrar = document.getElementById('btn-mostrar-form');
    if (btnMostrar) {
        btnMostrar.addEventListener('click', toggleFormulario);
    }

    const inputDataPasseio = document.getElementById('data-passeio');
    if (inputDataPasseio) {
        inputDataPasseio.addEventListener('blur', validarData);
    }
});

window.addEventListener("load", function() {
    document.querySelectorAll("form").forEach(form => form.reset());
});


function toggleFormulario() {
    const form = document.getElementById('formulario-cadastro');
    const botao = document.getElementById('btn-mostrar-form');

    if (form && botao) {
        form.classList.remove('d-none');
    } else {
        console.error("Erro: Um dos IDs do formulário ou botão não foi encontrado.");
    }
}

function aplicarMascaraData(campo) {
    let valor = campo.value;

    valor = valor.replace(/\D/g, "");
    valor = valor.slice(0, 8);
    if (valor.length > 2) {
        valor = valor.replace(/^(\d{2})(\d)/, "$1/$2");
    }

    if (valor.length > 5) {
        valor = valor.replace(/^(\d{2})\/(\d{2})(\d)/, "$1/$2/$3");
    }
    campo.value = valor;
}

function aplicarMascaraHora(campo) {
    let valor = campo.value;
    valor = valor.replace(/\D/g, "");

    valor = valor.slice(0, 4);

    if (valor.length > 2) {
        valor = valor.replace(/^(\d{2})(\d)/, "$1:$2");
    }

    campo.value = valor;
}

function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}

function validarData() {
    const campo = document.getElementById("data-passeio");
    const dataBr = campo.value.trim();
    let mensagemErro = "";

    if (dataBr === "") {
        campo.setCustomValidity("");
        return true;
    }

    if (dataBr.length != 10 || dataBr.indexOf('/') == -1)
        mensagemErro = "Formato de data incompleto. Use DD/MM/AAAA.";
    else {
        const partes = dataBr.split("/");
        const dia = parseInt(partes[0], 10);
        const mes = parseInt(partes[1], 10);
        const ano = parseInt(partes[2], 10);
        const dataInseridaObj = new Date(ano, mes - 1, dia);
        const dataAtual = new Date();

        if (dataInseridaObj.getFullYear() != ano || (dataInseridaObj.getMonth() + 1) != mes || dataInseridaObj.getDate() != dia)
            mensagemErro = "A data inserida não é válida!";
        else
            if (dataInseridaObj.getTime() <= dataAtual.getTime())
                mensagemErro = "Não é possível agendar uma data anterior ou igual à data atual.";
    }

    campo.setCustomValidity(mensagemErro);
    if (mensagemErro !== "")
        campo.reportValidity();
    return mensagemErro === "";
}

function cadastrarPasseio(event) {
    event.preventDefault();

    const form = document.getElementById("formPasseio");
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

function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-passeio");
    if(container) {
        container.innerHTML = texto;
        container.className = 'mensagem ' + (sucesso ? 'sucesso' : 'erro');
    }
}
