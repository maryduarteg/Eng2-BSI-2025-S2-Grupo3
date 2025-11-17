document.addEventListener("DOMContentLoaded", () => {
    const API = "http://localhost:8080/apis/presenca/oficina";
    const selectOficinaData = document.getElementById("select-oficina-data");
    const tabelaAlunos = document.querySelector("#tabela-alunos tbody");
    const btnSalvar = document.getElementById("btn-salvar-faltas");
    const mensagem = document.getElementById("mensagem-chamada");

    // Elementos de foto
    const secaoFotos = document.getElementById("secao-fotos");
    const fotoInput = document.getElementById("foto-input");
    const fotoDescricao = document.getElementById("foto-descricao");
    const btnEnviarFotos = document.getElementById("btn-enviar-fotos");
    const btnPularFotos = document.getElementById("btn-pular-fotos");

    let dmfIdAtual = null;

    function carregarDatasOficinas() {
        fetch(`${API}/datas`)
            .then(r => r.json())
            .then(lista => {
                selectOficinaData.innerHTML = `<option value="">Selecione...</option>`;
                lista.forEach(item => {
                    let opt = document.createElement("option");
                    opt.value = item.dmf_id;
                    opt.textContent = item.descricao;
                    selectOficinaData.appendChild(opt);
                });
            })
            .catch(() => {
                mensagem.textContent = "Erro ao carregar datas das oficinas.";
                mensagem.className = "alert alert-danger";
            });
    }

    selectOficinaData.addEventListener("change", function () {
        const dmf_id = parseInt(this.value);
        dmfIdAtual = dmf_id;

        mensagem.textContent = "";
        mensagem.className = "";
        tabelaAlunos.innerHTML = "";
        secaoFotos.style.display = "none";

        if (!dmf_id) return;

        fetch(`${API}/chamada-feita/${dmf_id}`)
            .then(r => r.json())
            .then(res => {
                if (res.chamadaFeita === true) {
                    mensagem.textContent = "A chamada já foi realizada para essa data!";
                    mensagem.className = "alert alert-warning";
                    btnSalvar.disabled = true;
                    return;
                }

                fetch(`${API}/alunos/${dmf_id}`)
                    .then(r => r.json())
                    .then(lista => {
                        tabelaAlunos.innerHTML = "";
                        btnSalvar.disabled = false;

                        lista.forEach(a => {
                            tabelaAlunos.innerHTML += `
                                <tr>
                                    <td>${a.nome}</td>
                                    <td class="text-center">
                                        <input type="checkbox" 
                                               class="form-check-input" 
                                               data-aluno-id="${a.alu_id}"
                                               ${a.faltou ? 'checked' : ''}>
                                    </td>
                                </tr>
                            `;
                        });
                    });
            });
    });

    btnSalvar.addEventListener("click", () => {
        const checkboxes = document.querySelectorAll("#tabela-alunos input[type='checkbox']:checked");
        const faltas = Array.from(checkboxes).map(cb => parseInt(cb.dataset.alunoId));

        if (!dmfIdAtual) {
            alert("Selecione uma data primeiro!");
            return;
        }

        // Registrar faltas
        let promises = [];
        faltas.forEach(aluId => {
            promises.push(
                fetch(`${API}`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ idAluno: aluId, idDia: dmfIdAtual })
                })
            );
        });

        Promise.all(promises)
            .then(() => {
                mensagem.textContent = `${faltas.length} falta(s) registrada(s) com sucesso!`;
                mensagem.className = "alert alert-success";

                // Mostrar seção de fotos opcionais
                secaoFotos.style.display = "block";
                btnSalvar.disabled = true;
            })
            .catch(() => {
                mensagem.textContent = "Erro ao salvar faltas.";
                mensagem.className = "alert alert-danger";
            });
    });

    // Validar número de fotos (máximo 2)
    fotoInput.addEventListener("change", function(e) {
        if (e.target.files.length > 2) {
            alert("Máximo de 2 fotos permitidas!");
            e.target.value = "";
        }
    });

    // Enviar fotos
    btnEnviarFotos.addEventListener("click", () => {
        const files = fotoInput.files;
        const descricao = fotoDescricao.value.trim();

        if (files.length === 0) {
            alert("Selecione pelo menos uma foto!");
            return;
        }

        if (!descricao) {
            alert("Digite uma descrição para as fotos!");
            return;
        }

        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
        }
        formData.append("dmf_id", dmfIdAtual);
        formData.append("fto_descricao", descricao);

        fetch(`${API}/fotos`, {
            method: "POST",
            body: formData
        })
            .then(r => r.json())
            .then(res => {
                if (res.mensagem) {
                    alert(res.mensagem);
                    limparSecaoFotos();
                } else if (res.erro) {
                    alert("Erro: " + res.erro);
                }
            })
            .catch(err => {
                alert("Erro ao enviar fotos: " + err.message);
            });
    });

    // Pular fotos
    btnPularFotos.addEventListener("click", () => {
        alert("Chamada finalizada sem fotos.");
        limparSecaoFotos();
    });

    function limparSecaoFotos() {
        secaoFotos.style.display = "none";
        fotoInput.value = "";
        fotoDescricao.value = "";
    }

    // Inicializar
    carregarDatasOficinas();
});
