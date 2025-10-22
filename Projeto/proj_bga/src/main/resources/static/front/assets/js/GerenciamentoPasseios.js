document.addEventListener('DOMContentLoaded', (event) => {
    const btnMostrar = document.getElementById('btn-mostrar-form');
    if (btnMostrar) {
        btnMostrar.addEventListener('click', toggleFormulario);
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

function cadastrarPasseio(event) {
    event.preventDefault();

    const form = document.getElementById("formPasseio");
    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return false;
    }

    // Converter a data para formato ISO (yyyy-MM-dd)
    function converterDataBrasilParaISO(dataBr) {
        const [dia, mes, ano] = dataBr.split("/");
        return `${ano}-${mes}-${dia}`;
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
