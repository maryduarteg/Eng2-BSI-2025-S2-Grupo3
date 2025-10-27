document.addEventListener('DOMContentLoaded', (event) => {
    const btnMostrar = document.getElementById('btn-mostrar-form');
    if (btnMostrar)
        btnMostrar.addEventListener('click', toggleFormulario);
});

/*window.addEventListener("load", function() {
    document.querySelectorAll("form").forEach(form => form.reset());
});*/

function toggleFormulario() {
    const form = document.getElementById('formulario-cadastro');
    const botao = document.getElementById('btn-mostrar-form');

    if (form && botao) {
        form.classList.remove('d-none');
    } else {
        console.error("Erro: Um dos IDs do formulário ou botão não foi encontrado!.");
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


document.addEventListener('DOMContentLoaded', () =>
{
    const campos = Array.from(form.querySelectorAll(".form-control"));

    // Remove borda vermelha e mensagem ao digitar
    campos.forEach(campo => {
        campo.addEventListener("input", () => {
            campo.classList.remove("is-invalid");
            const erroMsg = campo.nextElementSibling;
            if (erroMsg && erroMsg.classList.contains("invalid-feedback")) {
                erroMsg.remove();
            }
        });
    });
});

const form = document.getElementById("formAlunoCadastrar");
const btnCadastrar = document.getElementById("submit-cadastrar");
btnCadastrar.addEventListener("click",function(e)
{
    e.preventDefault();

    //cadastrar aluno em função anônima

    const aluno = {
        dt_entrada: converterDataBrasilParaISO(form.dataEntrada.value),
        foto: form.foto.value,
        mae: form.nomeMae.value,
        pai: form.nomePai.value,
        responsavel_pais: form.responsavel.value,
        conhecimento: form.conhecimento.value,
        pais_convivem: form.paisConvivem.value,
        pensao: form.pensao.value,
        pes_id: "2"
    };

    console.log("JSON enviado:", aluno);

    fetch("http://localhost:8080/apis/aluno", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(aluno)
    })
        .then(resp => {
            console.log("Status:", resp.status);
            return resp.text();
        })
        .then(text => console.log("Resposta:", text))
        .catch(err => console.error("Erro:", err));
});

// Máscaras para datas e horas
document.getElementById("data-entrada").addEventListener("keyup", e => e.target.value = aplicarMascaraData(e.target.value));


function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}

function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}