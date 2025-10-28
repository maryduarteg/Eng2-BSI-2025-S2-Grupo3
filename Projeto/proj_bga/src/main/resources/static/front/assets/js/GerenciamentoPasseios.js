const IDS_CONTEUDO = [
    'formulario-cadastro',
    'tabela-container',
];
let secaoAtivaID = null;
let idEmEdicao = null;

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
        targetElement.classList.remove('d-none');
        secaoAtivaID = targetId;
        if (targetId === 'tabela-container') {
            carregarPasseios("", true);
        } else if (targetId === 'formulario-cadastro') {
            resetarFormulario();
        }
    } else {
        secaoAtivaID = null;
    }
}

function carregarPasseios(termoBusca = "", limparMensagem = true) {
    const tbody = document.getElementById("tabela-passeios-container");
    const msgContainer = document.getElementById("mensagem-tabela");
    const inputBusca = document.getElementById("input-busca-id");

    if (!tbody || !msgContainer) {
        console.error("ERRO CRÍTICO: Elemento HTML da Tabela não encontrado.");
        return;
    }

    if (limparMensagem) {
        msgContainer.className = '';
        msgContainer.textContent = '';
    }

    tbody.innerHTML = '';

    if (msgContainer.textContent === '') {
        msgContainer.textContent = termoBusca ?
            `Buscando por "${termoBusca}"...` :
            "Carregando todas as descrições...";
    }

    const url = "http://localhost:8080/apis/passeio" + (termoBusca ? `?filtro=${encodeURIComponent(termoBusca)}` : "");

    fetch(url)
        .then(response => {
            if (response.status === 400) {
                return response.json().then(data => {
                    if (limparMensagem) {
                        msgContainer.className = '';
                    }
                    msgContainer.textContent = data.mensagem || "Nenhuma descrição cadastrada.";
                    return [];
                });
            }
            if (!response.ok) {
                throw new Error('Falha na resposta da rede: ' + response.statusText);
            }
            return response.json();
        })
        .then(listaDescricoes => {
            if (listaDescricoes.length === 0) {
                msgContainer.textContent = termoBusca ?
                    `Nenhuma descrição encontrada para "${termoBusca}".` :
                    "Nenhuma descrição cadastrada.";
                msgContainer.className = '';
                return;
            }

            if (limparMensagem) {
                msgContainer.textContent = '';
                msgContainer.className = '';
            }

            listaDescricoes.forEach(pde => {
                const row = tbody.insertRow();
                const descricaoSegura = pde.pde_descricao.replace(/'/g, "\\'");

                row.innerHTML = `
                        <td>${pde.pde_id}</td>
                        <td>${pde.pde_descricao}</td>
                        <td>
                            <button class="btn btn-sm btn-info" onclick="iniciarEdicao(${pde.pde_id}, '${descricaoSegura}')">
                                <i class="fas fa-pen"> </i>
                            </button>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-danger" onclick="confirmaExclusao(${pde.pde_id})">
                                <i class="fas fa-trash"> </i>
                            </button>
                        </td>
                    `;
            });
        })
        .catch(error => {
            console.error("Erro ao carregar descrições:", error);
            mostrarMensagemTabela("Erro ao carregar dados do servidor.", false);
        });
}

function buscarPasseiosFiltrados() {
    const inputBusca = document.getElementById("input-busca-id");
    const termo = inputBusca ? inputBusca.value.trim() : "";
    carregarPasseios(termo, true);
}

function limparBusca() {
    const inputBusca = document.getElementById("input-busca-id");
    if (inputBusca) {
        inputBusca.value = "";
    }
    carregarPasseios("", true);
}

function iniciarEdicao(id, descricao) {
    const formContainer = document.getElementById('formulario-cadastro');
    const tableContainer = document.getElementById('tabela-container');

    if (formContainer && tableContainer) {
        tableContainer.classList.add('d-none');
        formContainer.classList.remove('d-none');
        secaoAtivaID = 'formulario-cadastro';
    } else {
        mostrarMensagem("Erro: Contêineres não encontrados.", false);
        return;
    }

    const mensagemContainer = document.getElementById("mensagem-passeio");
    if (mensagemContainer) {
        mensagemContainer.innerHTML = '';
        mensagemContainer.className = 'mt-3';
    }

    idEmEdicao = id;
    const inputDescricao = document.getElementById("pde-descricao-input");

    if (inputDescricao) {
        inputDescricao.value = descricao;
    }

    const botaoSubmit = document.querySelector('#formPasseio button[type="submit"]');
    const tituloForm = document.querySelector('#formulario-cadastro h2');

    if (botaoSubmit) {
        botaoSubmit.textContent = "Atualizar Descrição #" + id;
    }
    if (tituloForm) {
        tituloForm.textContent = "Edição da Descrição #" + id;
    }
}

function submeterFormulario(event) {
    event.preventDefault();

    const form = document.getElementById("formPasseio");
    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return false;
    }

    const inputDescricao = document.getElementById("pde-descricao-input");
    if (!inputDescricao) {
        mostrarMensagem("Erro: Campo de descrição não encontrado.", false);
        return false;
    }

    const metodo = idEmEdicao !== null ? "PUT" : "POST";
    const url = "http://localhost:8080/apis/passeio";
    const dados = { pde_descricao: inputDescricao.value };

    if (idEmEdicao !== null) {
        dados.pde_id = idEmEdicao;
    }

    fetch(url, {
        method: metodo,
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams(dados),
    })
        .then(response => response.json())
        .then(res => {
            const acao = metodo === 'PUT' ? 'Atualizar' : 'Cadastrar';
            if (res.erro) {
                mostrarMensagem(`Erro ao ${acao}: ${res.erro || res.mensagem || 'Falha desconhecida.'}`, false);
                return;
            }
            const msgSucesso = res.mensagem || `Sucesso! Descrição ${acao === 'Atualizar' ? 'Atualizada' : 'Cadastrada'}.`;
            mostrarMensagem(msgSucesso, true);
            resetarFormulario();

            carregarPasseios("", true);
        })
        .catch(error => {
            console.error(`Erro ao ${metodo}:`, error);
            mostrarMensagem(`Erro de rede ao tentar ${metodo}`, false);
        });

    return false;
}

function resetarFormulario() {
    const form = document.getElementById("formPasseio");
    form.reset();
    form.classList.remove("was-validated");
    idEmEdicao = null;

    const botaoSubmit = document.querySelector('#formPasseio button[type="submit"]');
    const tituloForm = document.querySelector('#formulario-cadastro h2');

    if (botaoSubmit) {
        botaoSubmit.textContent = "Salvar Passeio";
    }
    if (tituloForm) {
        tituloForm.textContent = "Cadastro de Novo Passeio";
    }
}

function confirmaExclusao(id) {
    if(confirm(`Deseja realmente excluir a descrição com ID ${id}?`))
        executarExclusao(id);
}

function mostrarMensagemTabela(texto, sucesso) {
    const container = document.getElementById("mensagem-tabela");
    if(container) {
        container.className = 'mt-3';
        container.textContent = texto;
        container.classList.add('mensagem');
        container.classList.add(sucesso ? 'sucesso' : 'erro');
    }
}

function executarExclusao(id) {
    const inputBusca = document.getElementById("input-busca-id");
    const termoBusca = inputBusca ? inputBusca.value || "" : "";

    fetch(`http://localhost:8080/apis/passeio/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    }).then(response => {
        return response.json().then(data => ({ status: response.status, body: data }));
    })
        .then(({ status, body }) => {
            if (status === 200) {
                const mensagem = body.mensagem || `Passeio ${id} excluído com sucesso.`;
                mostrarMensagemTabela(mensagem, true);
                carregarPasseios(termoBusca, false);
            } else {
                const mensagem = body.mensagem || body.erro || `Erro ${status} ao excluir o passeio ${id}.`;
                mostrarMensagemTabela(mensagem, false);
            }
        })
        .catch(error => {
            console.error("Erro ao excluir:", error);
            mostrarMensagemTabela("Erro de rede ao tentar excluir o passeio.", false);
        });
}

function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-passeio");
    if(container) {
        container.className = 'mt-3';
        container.innerHTML = texto;
        container.classList.add('mensagem');
        container.classList.add(sucesso ? 'sucesso' : 'erro');
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    const divBotoes = document.getElementById('btn-group-master');
    if (divBotoes) {
        divBotoes.querySelectorAll('button').forEach(botao => {
            botao.addEventListener('click', toggleSecao);
        });
    }

    const btnBuscar = document.getElementById('btn-buscar-oficina');
    if (btnBuscar) {
        btnBuscar.addEventListener('click', buscarPasseiosFiltrados);
    }

    const btnLimpar = document.getElementById('btn-limpar-busca');
    if (btnLimpar) {
        btnLimpar.addEventListener('click', limparBusca);
    }
});

window.addEventListener("load", function() {
    document.querySelectorAll("form").forEach(form => form.reset());
});