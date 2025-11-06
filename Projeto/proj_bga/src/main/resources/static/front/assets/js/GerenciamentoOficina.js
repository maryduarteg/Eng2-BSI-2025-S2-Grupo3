document.addEventListener("DOMContentLoaded", () => {
    const btnMostrarForm = document.getElementById("btn-mostrar-form");
    const btnGerenciarOficinas = document.getElementById("btn-gerenciar-passeios");
    const formContainer = document.getElementById("formulario-cadastro");
    const tabelaContainer = document.getElementById("tabela-container");
    const formOficina = document.getElementById("formOficina");

    const descricaoInput = document.getElementById("ofc-descricao-input");
    const statusContainer = document.getElementById("status-container");
    const statusInput = document.getElementById("ofc-status-input");
    const mensagemDiv = document.getElementById("mensagem-oficina");

    let editandoId = null;

    // Mostrar formulário
    btnMostrarForm.addEventListener("click", () => {
        formContainer.classList.remove("d-none");
        tabelaContainer.classList.add("d-none");
        statusContainer.classList.add("d-none");
        limparFormulario();
    });

    // Mostrar tabela
    btnGerenciarOficinas.addEventListener("click", () => {
        formContainer.classList.add("d-none");
        tabelaContainer.classList.remove("d-none");
        listarOficinas();
    });

    // Submit do formulário
    formOficina.addEventListener("submit", async (e) => {
        e.preventDefault();
        mensagemDiv.innerHTML = "";

        if (!descricaoInput.value.trim()) {
            descricaoInput.classList.add("is-invalid");
            return;
        } else {
            descricaoInput.classList.remove("is-invalid");
        }

        const payload = {
            descricao: descricaoInput.value.trim(),
            ativo: statusInput.value
        };

        try {
            let response;
            if (editandoId) {
                payload.id = editandoId;
                response = await fetch("http://localhost:8080/apis/oficina", {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });
            } else {
                response = await fetch("http://localhost:8080/apis/oficina", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });
            }

            const result = await response.json();

            if (response.ok) {
                mensagemDiv.innerHTML = `<small class="text-success">${result.mensagem}</small>`;
                limparFormulario();
                listarOficinas();
            } else {
                mensagemDiv.innerHTML = `<small class="text-danger">${result.mensagem}</small>`;
            }
        } catch (error) {
            console.error(error);
            mensagemDiv.innerHTML = `<small class="text-danger">Erro ao conectar com o servidor</small>`;
        }
    });

    function limparFormulario() {
        descricaoInput.value = "";
        descricaoInput.classList.remove("is-invalid");
        statusInput.value = "A";
        editandoId = null;
        mensagemDiv.innerHTML = "";
    }

    async function listarOficinas() {
        tabelaContainer.innerHTML = "<p>Carregando...</p>";

        try {
            const res = await fetch("http://localhost:8080/apis/oficina");
            const data = await res.json();

            if (!res.ok) {
                tabelaContainer.innerHTML = `<p class="text-danger">${data.mensagem}</p>`;
                return;
            }

            if (data.length === 0) {
                tabelaContainer.innerHTML = "<p>Nenhuma oficina encontrada.</p>";
                return;
            }

            let html = `
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Descrição</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            data.forEach((o) => {
                html += `
                    <tr>
                        <td>${o.descricao}</td>
                        <td>${o.ativo === "S" ? "Ativo" : "Inativo"}</td>
                        <td>
                            <button class="btn btn-sm btn-primary me-2" onclick="editarOficina(${o.idOficina})">Editar</button>
                            <button class="btn btn-sm btn-danger" onclick="inativarOficina(${o.idOficina})">
                                ${o.ativo === "S" ? "Inativar" : "Ativar"}
                            </button>
                        </td>
                    </tr>
                `;
            });

            html += "</tbody></table>";
            tabelaContainer.innerHTML = html;

        } catch (error) {
            console.error(error);
            tabelaContainer.innerHTML = `<p class="text-danger">Erro ao carregar oficinas</p>`;
        }
    }

    window.editarOficina = async (id) => {
        try {
            const res = await fetch(`http://localhost:8080/apis/oficina/${id}`);
            const data = await res.json();

            if (!res.ok) {
                alert(data.mensagem);
                return;
            }

            descricaoInput.value = data.descricao;
            statusInput.value = data.ativo;
            statusContainer.classList.remove("d-none");
            formContainer.classList.remove("d-none");
            tabelaContainer.classList.add("d-none");
            editandoId = data.idOficina;

        } catch (error) {
            console.error(error);
            alert("Erro ao carregar oficina para edição");
        }
    };

    window.inativarOficina = async (id) => {
        try {
            const res = await fetch(`http://localhost:8080/apis/oficina/${id}`);
            const data = await res.json();

            if (!res.ok) {
                alert(data.mensagem);
                return;
            }

            const novoStatus = data.ativo === "S" ? "I" : "S";

            const payload = {
                id: data.idOficina,
                descricao: data.descricao,
                ativo: novoStatus
            };

            const updateRes = await fetch("http://localhost:8080/apis/oficina", {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            const updateData = await updateRes.json();
            if (updateRes.ok) {
                listarOficinas();
            } else {
                alert(updateData.mensagem);
            }

        } catch (error) {
            console.error(error);
            alert("Erro ao atualizar status");
        }
    };

    listarOficinas();
});
