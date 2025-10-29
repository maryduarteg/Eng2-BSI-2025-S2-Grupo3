//CADASTRO
document.addEventListener('DOMContentLoaded', (event) => {
    const btnMostrar = document.getElementById('btn-mostrar-form');
    if (btnMostrar)
        btnMostrar.addEventListener('click', toggleFormulario);

    //adiciona uma lista com as pessoas
    const selectPessoa = document.getElementById('pessoa');

    fetch("http://localhost:8080/apis/pessoa")
        .then(resp => {
            if (!resp.ok) throw new Error(`Erro HTTP: ${resp.status}`);
            return resp.json();
        })
        .then(listaPessoa => {
            console.log("Resposta da API:", listaPessoa);

            // Garante que o select existe e limpa o conteúdo
            selectPessoa.innerHTML = '';

            if (!listaPessoa || listaPessoa.length === 0) {
                console.log("Nenhuma pessoa cadastrada.");
                return;
            }

            // Preenche o select
            listaPessoa.forEach(pessoa => {
                const option = document.createElement('option');
                option.value = pessoa.id ?? "-";
                option.textContent = pessoa.nome ?? "-";
                selectPessoa.appendChild(option);
                console.log("Adicionando:", pessoa.nome);
            });
        })
        .catch(err => console.error("Erro capturado:", err));

});

function toggleFormulario() {
    const form = document.getElementById('formulario-cadastro');
    const botao = document.getElementById('btn-mostrar-form');

    if (form && botao) {
        form.classList.remove('d-none');
    } else {
        console.error("Erro: Um dos IDs do formulário ou botão não foi encontrado!.");
    }
}
document.getElementById("data-entrada").addEventListener("keyup", aplicarMascaraData);

function aplicarMascaraData(e) {
    let campo = e.target;
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
    const campos = Array.from(form.querySelectorAll(".form-control"));
    let valido = true;

    // Limpa erros
    campos.forEach(campo => {
        campo.classList.remove("is-invalid");
        const erroMsg = campo.nextElementSibling;
        if (erroMsg && erroMsg.classList.contains("invalid-feedback")) erroMsg.remove();
    });

    // Campos obrigatórios
    campos.forEach(campo => {
        if (!campo.value.trim()) {
            adicionarErro(campo, "Campo obrigatório");
            valido = false;
        }
    });

    if (!valido) return;

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
        pes_id: form.pessoa.value
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



function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}


function adicionarErro(campo, msg) {
    campo.classList.add("is-invalid");
    if (!campo.nextElementSibling || !campo.nextElementSibling.classList.contains("invalid-feedback")) {
        const div = document.createElement("div");
        div.className = "invalid-feedback";
        div.textContent = msg;
        campo.after(div);
    }
}

//EDIÇÃO
function carregarTodosAlunos()
{
    const tabela = document.getElementById("tabela-container");
    tabela.classList.remove("d-none");

    fetch("http://localhost:8080/apis/aluno")
        .then(resp => {
            if (!resp.ok) throw new Error(`Erro HTTP: ${resp.status}`);
            return resp.json();
        })
        .then(listaAluno => {
            console.log("Resposta da API:", listaPessoa);

            // Garante que o select existe e limpa o conteúdo
            selectPessoa.innerHTML = '';

            if (!listaAluno || listaAluno.length === 0) {
                console.log("Nenhum aluno cadastrado.");
                return;
            }
            const tablebody = document.getElementById("tabela-aluno-container");
            // Preenche a tabela
            listaAluno.forEach(aluno => {
                let linha = document.createElement("tr");
                let coluna = document.createElement("td");
                coluna.innerHTML = aluno.foto ?? "-";
                linha.appendChild(coluna);

                coluna.innerHTML = aluno.id ?? "-";
                linha.appendChild(coluna);

                coluna.innerHTML = aluno.dt_entrada ?? "-";
                linha.appendChild(coluna);

                coluna.innerHTML = aluno.mae ?? "-";
                linha.appendChild(coluna);

                coluna.innerHTML = aluno.pai ?? "-";
                linha.appendChild(coluna);

                coluna.innerHTML = aluno.responsavel ?? "-";
                linha.appendChild(coluna);

                coluna.innerHTML = aluno.ativo ?? "-";
                linha.appendChild(coluna);

                linha.appendChild(createBotaoEditar(aluno.id));
                linha.appendChild(createBotaoExcluir(aluno.id));
                tablebody.appendChild(linha);

            });
        })
        .catch(err => console.error("Erro capturado:", err));
}

function createBotaoEditar(id)
{
    let botao = document.createElement("button");
    botao.classList.add("btn", "btn-sm", "btn-outline-info", "btn-editar");
    let info = document.createElement("i");
    info.classList.add("fas", "fa-edit");
    botao.dataset.id = id;

    botao.appendChild(info);
    return botao;
}

function createBotaoExcluir(id)
{
    let botao = document.createElement("button");
    botao.classList.add("btn", "btn-sm", "btn-outline-info", "btn-excluir");
    let info = document.createElement("i");
    info.classList.add("fas", "fa-trash");
    botao.dataset.id = id;

    botao.appendChild(info);
    return botao;
}