document.addEventListener("DOMContentLoaded", () => {

    // ---------- ELEMENTOS ----------
    const btnMostrarForm = document.getElementById("btn-mostrar-form");
    const btnGerenciarOficinas = document.getElementById("btn-gerenciar-oficina");
    const formContainer = document.getElementById("formulario-cadastro");
    const tabelaContainer = document.getElementById("tabela-container");
    const formOficina = document.getElementById("formOficina");

    const descricaoInput = document.getElementById("ofc-descricao-input");
    const mensagemDiv = document.getElementById("mensagem-oficina");

    const formEditar = document.getElementById("formulario-editar-oficina");
    const btnSalvarEdicao = document.getElementById("btn-salvar-edicao");
    const mensagemEdicao = document.getElementById("mensagem-edicao");

    const inputId = document.getElementById("editar-id");
    const inputNome = document.getElementById("editar-nome");
    const selectAtivo = document.getElementById("editar-ativo");

    const filtroNome = document.getElementById("filtro-nome");
    const filtroStatus = document.getElementById("filtro-status");

    // ---------- FUNÇÕES ----------

    function limparFormulario() {
        descricaoInput.value = "";
        descricaoInput.classList.remove("is-invalid");
        mensagemDiv.innerHTML = "";
    }

    async function listarOficinas() {
        const tabelaDiv = document.getElementById("tabela-oficinas");
        tabelaDiv.innerHTML = "<p>Carregando...</p>";

        try {
            const res = await fetch("http://localhost:8080/apis/oficina");
            let data = await res.json();

            // FILTROS
            const nomeFiltro = filtroNome.value.toLowerCase();
            const statusFiltro = filtroStatus.value;

            data = data.filter(o =>
                o.descricao.toLowerCase().includes(nomeFiltro) &&
                (statusFiltro === "T" ? true : o.ativo === statusFiltro)
            );

            if (data.length === 0) {
                tabelaDiv.innerHTML = "<p>Nenhuma oficina encontrada.</p>";
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
                        <button class="btn btn-sm btn-outline-primary me-2"
                                onclick="abrirEdicaoOficina(${o.idOficina})"
                                title="Editar Oficina">
                            <i class="fas fa-edit"></i>
                        </button>

                        <button class="btn btn-sm btn-outline-danger"
                                onclick="inativarOficina(${o.idOficina})"
                                title="Inativar Oficina">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                `;
            });

            html += "</tbody></table>";
            tabelaDiv.innerHTML = html;

        } catch (error) {
            console.error(error);
            tabelaDiv.innerHTML = `<p class="text-danger">Erro ao carregar oficinas</p>`;
        }
    }

    // ---------- CADASTRO ----------
    btnMostrarForm.addEventListener("click", () => {
        formContainer.classList.remove("d-none");
        tabelaContainer.classList.add("d-none");
        formEditar.classList.add("d-none");
        limparFormulario();
    });

    btnGerenciarOficinas.addEventListener("click", () => {
        formContainer.classList.add("d-none");
        formEditar.classList.add("d-none");
        tabelaContainer.classList.remove("d-none");

        filtroNome.value = "";
        filtroStatus.value = "T";

        listarOficinas();
    });

    formOficina.addEventListener("submit", async (e) => {
        e.preventDefault();

        if (!descricaoInput.value.trim()) {
            descricaoInput.classList.add("is-invalid");
            return;
        }

        descricaoInput.classList.remove("is-invalid");

        const payload = { descricao: descricaoInput.value.trim(), ativo: "S" };

        try {
            const res = await fetch("http://localhost:8080/apis/oficina", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            const result = await res.json();
            mensagemDiv.innerHTML = `<small class="${res.ok ? "text-success" : "text-danger"}">${result.mensagem}</small>`;

            if (res.ok) limparFormulario();

        } catch (error) {
            mensagemDiv.innerHTML = `<small class="text-danger">Erro ao conectar</small>`;
        }
    });

    // ---------- EDIÇÃO ----------
    window.abrirEdicaoOficina = async (id) => {

        const res = await fetch(`http://localhost:8080/apis/oficina/${id}`);
        const data = await res.json();

        inputId.value = data.idOficina;
        inputNome.value = data.descricao;
        selectAtivo.value = data.ativo === "S" ? "A" : "I";

        formEditar.classList.remove("d-none");
        formContainer.classList.add("d-none");
        tabelaContainer.classList.add("d-none");
    };

    btnSalvarEdicao.addEventListener("click", async () => {
        const payload = {
            id: parseInt(inputId.value),
            descricao: inputNome.value.trim(),
            ativo: selectAtivo.value === "A" ? "S" : "I"
        };

        const res = await fetch("http://localhost:8080/apis/oficina", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const result = await res.json();
        mensagemEdicao.innerHTML = `<small class="${res.ok ? "text-success" : "text-danger"}">${result.mensagem}</small>`;

        if (res.ok) {
            formEditar.classList.add("d-none");
            btnGerenciarOficinas.click();
        }
    });

    // ---------- INATIVAR (SEM VOLTAR A ATIVAR) ----------
    window.inativarOficina = async (id) => {
        const res = await fetch(`http://localhost:8080/apis/oficina/${id}`);
        const data = await res.json();

        if (data.ativo !== "S") return; // já está inativa

        const payload = { id: data.idOficina, descricao: data.descricao, ativo: "I" };

        await fetch("http://localhost:8080/apis/oficina", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        listarOficinas();
    };

    // ---------- FILTROS ----------
    filtroNome.addEventListener("input", listarOficinas);
    filtroStatus.addEventListener("change", listarOficinas);

    // ---------- INICIAL ----------
    listarOficinas();
});
