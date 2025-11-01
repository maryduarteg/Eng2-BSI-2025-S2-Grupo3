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
            //console.log("Resposta da API:", listaPessoa);

            // Garante que o select existe e limpa o conteúdo
            selectPessoa.innerHTML = '';

            if (!listaPessoa || listaPessoa.length === 0) {
                //console.log("Nenhuma pessoa cadastrada.");
                return;
            }

            // Preenche o select
            listaPessoa.forEach(pessoa => {
                const option = document.createElement('option');
                option.value = pessoa.id ?? "-";
                option.textContent = pessoa.nome ?? "-";
                selectPessoa.appendChild(option);
                //console.log("Adicionando:", pessoa.nome);
            });
        })
        .catch(err => console.error("Erro capturado:", err));
    }
);

function toggleFormulario() {
    const form = document.getElementById('formulario-cadastro');
    const botao = document.getElementById('btn-mostrar-form');

    if (form && botao)
        form.classList.remove('d-none');
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

    //console.log("JSON enviado:", aluno);

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

function formatarDataParaBR(dataISO) {
    if (!dataISO) return "";
    const data = new Date(dataISO);
    if (isNaN(data)) return "";
    const dia = String(data.getDate()).padStart(2,"0");
    const mes = String(data.getMonth()+1).padStart(2,"0");
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
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
    let msg = document.getElementById("mensagem-tabela");
    msg.classList.remove("d-none");

    fetch("http://localhost:8080/apis/aluno")
        .then(resp => {
            if (!resp.ok) throw new Error(`Erro HTTP: ${resp.status}`);
            return resp.json();
        })
        .then(listaAluno => {
            //console.log("Resposta da API:", listaAluno);

            if (!listaAluno || listaAluno.length === 0) {
                //console.log("Nenhum aluno cadastrado.");
                return;
            }
            const tablebody = document.getElementById("tabela-aluno-container");
            tablebody.innerHTML = "";
            // Preenche a tabela
            listaAluno.forEach(aluno => {
                let linha = document.createElement("tr");

                let tdFoto = document.createElement("td");
                tdFoto.innerHTML = aluno.foto ?? "-";
                linha.appendChild(tdFoto);

                let tdId = document.createElement("td");
                tdId.innerHTML = aluno.id ?? "-";
                linha.appendChild(tdId);

                let tdEntrada = document.createElement("td");
                tdEntrada.innerHTML = formatarDataParaBR(aluno.dt_entrada) ?? "-";
                linha.appendChild(tdEntrada);

                let tdMae = document.createElement("td");
                tdMae.innerHTML = aluno.mae ?? "-";
                linha.appendChild(tdMae);

                let tdPai = document.createElement("td");
                tdPai.innerHTML = aluno.pai ?? "-";
                linha.appendChild(tdPai);

                let tdResponsavel = document.createElement("td");
                tdResponsavel.innerHTML = aluno.responsavel_pais ?? "-";
                linha.appendChild(tdResponsavel);

                let tdAtivo = document.createElement("td");
                tdAtivo.innerHTML = aluno.ativo ?? "-";
                linha.appendChild(tdAtivo);

                // Botões
                let tdAlterar = document.createElement("td");
                tdAlterar.appendChild(createBotaoEditar(aluno.id, aluno.ativo));
                let tdAtivDes = document.createElement("td");
                tdAtivDes.appendChild(createBotaoAtivDes(aluno.pes_id, aluno.ativo));

                linha.appendChild(tdAlterar);
                linha.appendChild(tdAtivDes);
                tablebody.appendChild(linha);
                //console.log("Adicionando:", aluno.id)
                msg.classList.add("d-none");

                let botoesEditar = document.getElementsByClassName("btn-editar");
                let botoesAtivDes = document.getElementsByClassName("btn-ativdes");

                Array.from(botoesEditar).forEach(e => {
                    e.addEventListener("click", function() {
                        if (e.dataset.status === 'N')
                            console.log("Este registro está desativado. Não pode ser alterado");
                    });
                });

                Array.from(botoesAtivDes).forEach(e => {
                    e.addEventListener("click", () => ativarDesativarRegistro(e.dataset.id));
                });
            });
        })
        .catch(err => console.error("Erro capturado:", err));


}

function createBotaoEditar(id, status)
{
    let botao = document.createElement("button");
    botao.classList.add("btn", "btn-sm", "btn-outline-info", "btn-editar");
    let info = document.createElement("i");
    info.classList.add("fas", "fa-edit");
    botao.dataset.id = id;
    botao.dataset.status = status;
    botao.appendChild(info);
    return botao;
}

function createBotaoAtivDes(id, status)
{
    let botao = document.createElement("button");
    botao.classList.add("btn", "btn-sm", "btn-outline-info", "btn-ativdes");
    let info = document.createElement("i");


    if(status === 'S')
        info.classList.add("fas", "fa-trash");
    else
        info.classList.add("fas", "fa-check");

    botao.dataset.status = status;
    botao.dataset.id = id;

    botao.appendChild(info);
    return botao;
}

function ativarDesativarRegistro(id) {

    fetch(`http://localhost:8080/apis/pessoa/${id}`)
        .then(response => {
            if (!response.ok) throw new Error('Pessoa não encontrada');
            return response.json(); // aqui!
        })
        .then(pessoa => {
            let ativ = pessoa.ativo === 'S' ? 'N' : 'S';

            const pes = {
                id: pessoa.id,
                nome: pessoa.nome,
                cpf: pessoa.cpf,
                dt_nascimento: pessoa.dtnasc,
                rg: pessoa.rg,
                ativo: ativ,
                end_id: pessoa.end
            };

            console.log(pes);

            fetch("http://localhost:8080/apis/pessoa", {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(pes)
            })
                .then(resp => {
                    console.log("Status:", resp.status);
                    return resp.text();
                })
                .then(text => console.log("Resposta:", text))
                .catch(err => console.error("Erro:", err));
        })
        .catch(err => console.error(err));
}