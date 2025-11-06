document.addEventListener("DOMContentLoaded", () => {

    const API = "http://localhost:8080/apis/presenca/oficina";

    const selectOficinaData = document.getElementById("select-oficina-data");
    const tabelaAlunos = document.querySelector("#tabela-alunos tbody");
    const btnSalvar = document.getElementById("btn-salvar-faltas");
    const mensagem = document.getElementById("mensagem-chamada");

    function carregarDatasOficinas() {
        fetch(`${API}/datas`)
            .then(r => r.json())
            .then(lista => {
                selectOficinaData.innerHTML = `<option value="">Selecione uma oficina e data</option>`;
                lista.forEach(item => {
                    let opt = document.createElement("option");
                    opt.value = item.dmf_id;
                    opt.textContent = item.descricao;
                    selectOficinaData.appendChild(opt);
                });
            })
            .catch(() => {
                mensagem.textContent = "Erro ao carregar datas das oficinas.";
            });
    }

    selectOficinaData.addEventListener("change", function () {
        const dmf_id = parseInt(this.value);
        mensagem.textContent = "";
        tabelaAlunos.innerHTML = "";

        if (!dmf_id) return;

        fetch(`${API}/chamada-feita/${dmf_id}`)
            .then(r => r.json())
            .then(res => {
                if (res.chamadaFeita === true) {
                    mensagem.textContent = "A chamada já foi realizada para essa data!";
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
                                    <td><input type="checkbox" class="chk-falta" data-alu="${a.alu_id}"></td>
                                </tr>
                            `;
                        });
                    })
                    .catch(() => {
                        mensagem.textContent = "Erro ao carregar alunos.";
                    });
            })
            .catch(() => {
                mensagem.textContent = "Erro ao verificar se a chamada já foi realizada.";
            });
    });

    btnSalvar.addEventListener("click", () => {
        const dmf_id = parseInt(selectOficinaData.value);
        mensagem.textContent = "";

        if (!dmf_id) {
            mensagem.textContent = "Selecione uma oficina e data primeiro.";
            return;
        }

        const faltas = document.querySelectorAll(".chk-falta:checked");

        if (faltas.length === 0) {
            mensagem.textContent = "Nenhuma falta marcada.";
            return;
        }

        Promise.all(Array.from(faltas).map(chk => {
            const dto = {
                idAluno: parseInt(chk.dataset.alu),
                idDia: dmf_id
            };
            return fetch(`${API}`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(dto)
            });
        })).then(() => {
            mensagem.style.color = "green";
            mensagem.textContent = "Faltas registradas com sucesso ✅";
            selectOficinaData.value = "";
            tabelaAlunos.innerHTML = "";
            btnSalvar.disabled = true;
        }).catch(() => {
            mensagem.style.color = "red";
            mensagem.textContent = "Erro ao registrar faltas.";
        });
    });

    carregarDatasOficinas();
});
