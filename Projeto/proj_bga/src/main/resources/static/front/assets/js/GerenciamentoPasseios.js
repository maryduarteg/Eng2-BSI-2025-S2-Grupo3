const IDS_CONTEUDO = [
    'formulario-cadastro',
    'tabela-container',
];
let secaoAtivaID = null;

// ====================================================================

function formatarDataParaBR(dataISO) {
    const dataObj = new Date(dataISO);
    const dia = String(dataObj.getDate()).padStart(2, '0');
    const mes = String(dataObj.getMonth() + 1).padStart(2, '0');
    const ano = dataObj.getFullYear();
    return `${dia}/${mes}/${ano}`;
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

// ====================================================================

function validarData() {
    const campo = document.getElementById("data-passeio");
    if (!campo) return false;

    const dataBr = campo.value.trim();
    let mensagemErro = "";

    if (dataBr === "") {
        campo.setCustomValidity("");
        return true;
    }

    if (dataBr.length !== 10 || dataBr.indexOf('/') === -1) {
        mensagemErro = "Formato de data incompleto. Use DD/MM/AAAA.";
    } else {
        const partes = dataBr.split("/");
        const dia = parseInt(partes[0], 10);
        const mes = parseInt(partes[1], 10);
        const ano = parseInt(partes[2], 10);

        // Validação de calendário e data futura
        const dataInseridaObj = new Date(ano, mes - 1, dia);
        const dataAtual = new Date();
        dataAtual.setHours(0, 0, 0, 0); // Zera a hora para comparação

        if (dataInseridaObj.getFullYear() !== ano || (dataInseridaObj.getMonth() + 1) !== mes || dataInseridaObj.getDate() !== dia) {
            mensagemErro = "A data inserida não é válida!";
        } else if (dataInseridaObj.getTime() <= dataAtual.getTime()) {
            mensagemErro = "Não é possível agendar uma data anterior ou igual à data atual.";
        }
    }

    campo.setCustomValidity(mensagemErro);
    if (mensagemErro !== "") {
        campo.reportValidity();
    }
    return mensagemErro === "";
}

function toggleSecao(event) {
    const botaoClicado = event.target.closest('button');
    if (!botaoClicado)
        return;

    const targetId = botaoClicado.getAttribute('data-target-id');
    if (!targetId)
        return;

    IDS_CONTEUDO.forEach(id => {
        const container = document.getElementById(id);
        if (container) {
            container.classList.add('d-none');
        }
    });

    const targetElement = document.getElementById(targetId);
    if (targetElement && targetId !== secaoAtivaID) {
        targetElement.classList.remove('d-none'); // MOSTRA
        secaoAtivaID = targetId;

        if (targetId === 'tabela-container') {
            carregarTodosPasseios();
        }
    } else {
        secaoAtivaID = null;
    }
}

// ====================================================================

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
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams(dados),
    })
        .then(response => response.json())
        .then(res => {
            if(res.erro) {
                mostrarMensagem("Erro: " + res.erro, false);
                return;
            }
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

function carregarTodosPasseios() {
    const tbody = document.getElementById("tabela-passeios-container");
    const msgContainer = document.getElementById("mensagem-tabela");

    if (!tbody || !msgContainer) {
        console.error("ERRO CRÍTICO: Elemento HTML da Tabela não encontrado.");
        return;
    }

    tbody.innerHTML = '';
    msgContainer.textContent = "Carregando todos os passeios...";

    fetch("http://localhost:8080/apis/passeio")
        .then(response => {
            if (response.status === 400) {
                return response.json().then(data => {
                    msgContainer.textContent = data.mensagem || "Nenhum passeio cadastrado.";
                    return [];
                });
            }
            if (!response.ok) {
                throw new Error('Falha na resposta da rede: ' + response.statusText);
            }
            return response.json();
        })
        .then(listaPasseios => {
            if (listaPasseios.length === 0) {
                msgContainer.textContent = "Nenhum passeio cadastrado.";
                return;
            }

            msgContainer.textContent = '';

            listaPasseios.forEach(passeio => {
                const row = tbody.insertRow();

                row.innerHTML = `
                        <td>${passeio.id}</td>
                        <td>${formatarDataParaBR(passeio.data)}</td>
                        <td>${passeio.hora_inicio}</td>
                        <td>${passeio.hora_final}</td>
                        <td>${passeio.chamada_feita === 'S' ? 'Feita' : 'Pendente'}</td>
                        <td>${passeio.pde_id}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-info"> //botão de editar
                                <i class="fas fa-edit"> </i>
                            </button>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-danger"> // botão de excluir
                                <i class="fas fa-trash"> </i>
                            </button>
                        </td>
                    `;
            });
        })
        .catch(error => {
            console.error("Erro ao carregar passeios:", error);
            msgContainer.textContent = "Erro ao carregar dados do servidor.";
        });
}

function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-passeio");
    if(container) {
        container.innerHTML = texto;
        container.className = 'mensagem ' + (sucesso ? 'sucesso' : 'erro');
    }
}


// ====================================================================

document.addEventListener('DOMContentLoaded', (event) => {
    // 1. Anexar o TOGGLE universal a todos os botões no grupo mestre
    const divBotoes = document.getElementById('btn-group-master');
    if (divBotoes) {
        divBotoes.querySelectorAll('button').forEach(botao => {
            botao.addEventListener('click', toggleSecao);
        });
    }

    const inputDataPasseio = document.getElementById('data-passeio');
    if (inputDataPasseio) {
        inputDataPasseio.addEventListener('blur', validarData);
    }

    const inputHoraInicio = document.getElementById('hora-inicio');
    const inputHoraFim = document.getElementById('hora-fim');
    if (inputHoraInicio) {
        inputHoraInicio.addEventListener('blur', () => validarHora(inputHoraInicio));
    }
    if (inputHoraFim) {
        inputHoraFim.addEventListener('blur', () => validarHora(inputHoraFim));
    }

});

window.addEventListener("load", function() {
    document.querySelectorAll("form").forEach(form => form.reset());
});