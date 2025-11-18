const API_BASE_URL = 'http://localhost:8080/apis/associa-aluno-oficina';
let isProcessing = false;

async function listarOficinas() {
    console.log('Iniciando busca de oficinas...');

    try {
        const response = await fetch(`${API_BASE_URL}/oficinas`);

        console.log('Resposta recebida:', response.status);

        if (!response.ok) {
            throw new Error(`Erro HTTP ${response.status}: ${response.statusText}`);
        }

        const oficinas = await response.json();
        console.log('Oficinas recebidas:', oficinas);

        if (!oficinas || oficinas.length === 0) {
            alert('Nenhuma oficina com oferta ativa encontrada no banco de dados!');
            return;
        }

        mostrarOficinas(oficinas);

    } catch (error) {
        console.error('Erro ao buscar oficinas:', error);
        alert('Erro ao carregar oficinas:\n\n' + error.message +
            '\n\nVerifique se o backend está rodando em http://localhost:8080');
    }
}

function mostrarOficinas(oficinas) {
    console.log('Renderizando', oficinas.length, 'oficinas...');

    const tbody = document.getElementById('tabelaOficinas');
    const container = document.getElementById('oficinasContainer');

    tbody.innerHTML = '';

    oficinas.forEach(oficina => {
        const tr = document.createElement('tr');

        // USA APENAS O ID DA OFERTA
        const ofertaId = oficina.id || oficina.idOferta;

        tr.id = `oficina-row-${ofertaId}`;

        const horaInicio = oficina.horaInicio || 'N/A';
        const horaFim = oficina.horaFim || 'N/A';
        const dataInicio = oficina.dataInicio ? formatarData(oficina.dataInicio) : 'N/A';
        const dataFim = oficina.dataFim ? formatarData(oficina.dataFim) : 'N/A';

        const statusBadge = oficina.ativo === 'S' ?
            '<span class="badge bg-success">Ativa</span>' :
            '<span class="badge bg-danger">Inativa</span>';

        const desc = String(oficina.descricao ?? '').replace(/'/g, "\\'");

        tr.innerHTML = `
            <td class="text-center">${ofertaId}</td>
            <td>
                <strong>${oficina.descricao ?? '—'}</strong><br>
                <small class="text-muted">
                    <i class="far fa-clock"></i> ${horaInicio} - ${horaFim}<br>
                    <i class="far fa-calendar"></i> ${dataInicio} a ${dataFim}
                </small>
            </td>
            <td class="text-center">${statusBadge}</td>
            <td class="text-center">
                <div class="btn-group" role="group">
                    <button class="btn btn-primary btn-sm"
                            onclick="mostrarAlunosDisponiveis(${ofertaId}, '${desc}')">
                        <i class="fas fa-user-plus"></i> Vincular
                    </button>
                    <button class="btn btn-info btn-sm"
                            onclick="visualizarAlunosVinculados(${ofertaId}, '${desc}')">
                        <i class="fas fa-eye"></i> Vinculados
                    </button>
                </div>
            </td>
        `;

        tbody.appendChild(tr);
    });

    container.style.display = 'block';
    console.log(oficinas.length + ' oficinas exibidas com sucesso!');
}

async function mostrarAlunosDisponiveis(ofertaId, nomeOficina) {
    try {
        const containerAntigo = document.getElementById(`alunos-container-${ofertaId}`);
        if (containerAntigo) {
            containerAntigo.remove();
        }

        // USA ID DA OFERTA
        const response = await fetch(`${API_BASE_URL}/alunos-disponiveis/${ofertaId}`);

        if (!response.ok) {
            throw new Error(`Erro HTTP ${response.status}: ${response.statusText}`);
        }

        const alunosDisponiveis = await response.json();
        console.log('Alunos disponíveis recebidos:', alunosDisponiveis);

        if (!alunosDisponiveis || alunosDisponiveis.length === 0) {
            alert('⚠️ Não há alunos disponíveis para esta oficina.');
            return;
        }

        exibirAlunosDisponiveis(alunosDisponiveis, ofertaId, nomeOficina);

    } catch (error) {
        console.error('Erro ao buscar alunos disponíveis:', error);
        alert('Erro ao buscar alunos disponíveis:\n\n' + error.message);
    }
}


function exibirAlunosDisponiveis(alunos, oficinaId, nomeOficina) {
    console.log(`Exibindo ${alunos.length} alunos disponíveis...`);

    // Esconde todas as oficinas
    const todasOficinas = document.querySelectorAll('[id^="oficina-row-"]');
    todasOficinas.forEach(row => row.style.display = 'none');

    // Mostra apenas a oficina selecionada
    const oficinaRow = document.getElementById(`oficina-row-${oficinaId}`);
    if (oficinaRow) {
        oficinaRow.style.display = '';
    }

    // Remove container anterior se existir
    const containerAntigo = document.getElementById(`alunos-container-${oficinaId}`);
    if (containerAntigo) {
        containerAntigo.remove();
    }

    // Cria novo container para alunos disponÃ­veis
    const container = document.createElement('div');
    container.id = `alunos-container-${oficinaId}`;
    container.className = 'mt-4';
    container.innerHTML = `
        <div class="card border-primary">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">
                    <i class="fas fa-users"></i> Alunos Disponíveis: ${nomeOficina}
                </h5>
                <small>Total: ${alunos.length} aluno(s) disponível(is)</small>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead class="table-primary">
                            <tr>
                                <th style="width: 8%;">ID</th>
                                <th>Nome</th>
                                <th>MÃ£e</th>
                                <th>Pai</th>
                                <th style="width: 10%;" class="text-center">Status</th>
                                <th style="width: 12%;" class="text-center">Ação</th>
                            </tr>
                        </thead>
                        <tbody id="tbody-alunos-${oficinaId}">
                        </tbody>
                    </table>
                </div>
                <div class="alert alert-info mb-0" role="alert">
                    <i class="fas fa-info-circle"></i> 
                    <strong>Dica:</strong> Clique em "Vincular" para associar o aluno a esta oficina.
                </div>
                <button class="btn btn-secondary mt-3" onclick="voltarParaOficinas()">
                    <i class="fas fa-arrow-left"></i> Voltar para Oficinas
                </button>
            </div>
        </div>
    `;

    const oficinasContainer = document.getElementById('oficinasContainer');
    oficinasContainer.parentNode.insertBefore(container, oficinasContainer.nextSibling);

    // Preenche a tabela com os alunos disponíveis
    const tbody = document.getElementById(`tbody-alunos-${oficinaId}`);
    alunos.forEach(aluno => {
        const tr = document.createElement('tr');
        tr.id = `aluno-row-${aluno.id}`;

        const statusBadge = aluno.ativo === 'S' ?
            '<span class="badge bg-success">Ativo</span>' :
            '<span class="badge bg-danger">Inativo</span>';

        tr.innerHTML = `
            <td class="text-center">${aluno.id}</td>
            <td><strong>${aluno.nome}</strong></td>
            <td>${aluno.mae || 'N/A'}</td>
            <td>${aluno.pai || 'N/A'}</td>
            <td class="text-center">${statusBadge}</td>
            <td class="text-center">
                <button class="btn btn-success btn-sm" 
                        onclick="vincularAluno(${aluno.id}, ${oficinaId})">
                    <i class="fas fa-link"></i> Vincular
                </button>
            </td>
        `;

        tbody.appendChild(tr);
    });

    console.log('Alunos disponíveis exibidos com sucesso!');
}

async function vincularAluno(alunoId, ofertaId) {
    if (isProcessing) {
        console.log('Aguarde, processando...');
        return;
    }

    isProcessing = true;
    const btn = event ? event.target : null;

    if (btn) {
        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Aguarde...';
    }

    try {
        console.log(`Vinculando aluno ${alunoId} à oferta ${ofertaId}`);

        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                alu_id: Number(alunoId),
                ofc_id: Number(ofertaId)  // Usa diretamente
            })
        });

        const result = await response.json();
        console.log('Resposta:', result);

        if (result.erro) {
            throw new Error(result.erro);
        }

        console.log('Vinculação bem-sucedida!');
        alert('Aluno vinculado com sucesso!');

        // Remove a linha do aluno da tabela
        const row = document.getElementById(`aluno-row-${alunoId}`);
        if (row) {
            row.remove();
        }

        // Verifica se ainda há alunos disponíveis
        const tbody = document.getElementById(`tbody-alunos-${ofertaId}`);
        if (tbody && tbody.children.length === 0) {
            alert('Todos os alunos disponíveis foram vinculados a esta oficina!');
            voltarParaOficinas();
        }

    } catch (error) {
        console.error('Erro:', error);
        alert('❌ Erro: ' + error.message);
    } finally {
        isProcessing = false;
        if (btn) {
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-link"></i> Vincular';
        }
    }
}

async function visualizarAlunosVinculados(ofertaId, nomeOficina) {
    console.log(`Visualizando alunos vinculados à oferta ${ofertaId}...`);

    try {
        // Busca todos os vínculos
        const responseVinculos = await fetch(API_BASE_URL);
        if (!responseVinculos.ok) {
            throw new Error('Erro ao buscar vínculos');
        }
        const todosVinculos = await responseVinculos.json();
        console.log('Todos os vínculos:', todosVinculos);

        // USA O ID DA OFERTA DIRETAMENTE (sem mapeamento)
        const vinculosOficina = todosVinculos.filter(v => Number(v.ofcId) === Number(ofertaId));
        console.log(`Vínculos da oferta ${ofertaId}:`, vinculosOficina);

        if (vinculosOficina.length === 0) {
            alert('Nenhum aluno vinculado a esta oficina ainda.');
            return;
        }

        // Busca dados completos dos alunos
        const responseAlunos = await fetch(`${API_BASE_URL}/alunos`);
        if (!responseAlunos.ok) {
            throw new Error('Erro ao buscar alunos');
        }
        const todosAlunos = await responseAlunos.json();

        // Filtra apenas os alunos vinculados
        const idsVinculados = vinculosOficina.map(v => v.aluId);
        const alunosVinculados = todosAlunos.filter(a => idsVinculados.includes(a.id));

        console.log('Alunos vinculados:', alunosVinculados);

        // Exibe os alunos vinculados
        exibirAlunosVinculados(alunosVinculados, ofertaId, nomeOficina);

    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao buscar alunos vinculados: ' + error.message);
    }
}



function exibirAlunosVinculados(alunos, oficinaId, nomeOficina) {
    // Esconde todas as oficinas
    const todasOficinas = document.querySelectorAll('[id^="oficina-row-"]');
    todasOficinas.forEach(row => row.style.display = 'none');

    // Mostra apenas a oficina selecionada
    const oficinaRow = document.getElementById(`oficina-row-${oficinaId}`);
    if (oficinaRow) {
        oficinaRow.style.display = '';
    }

    // Remove container anterior se existir
    const containerAntigo = document.getElementById(`vinculados-container-${oficinaId}`);
    if (containerAntigo) {
        containerAntigo.remove();
    }

    // Cria novo container para alunos vinculados
    const container = document.createElement('div');
    container.id = `vinculados-container-${oficinaId}`;
    container.className = 'mt-4';
    container.innerHTML = `
        <div class="card border-info">
            <div class="card-header bg-info text-white">
                <h5 class="mb-0">
                    <i class="fas fa-users"></i> Alunos Vinculados: ${nomeOficina}
                </h5>
                <small>Total: ${alunos.length} aluno(s)</small>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead class="table-info">
                            <tr>
                                <th style="width: 8%;">ID</th>
                                <th>Nome</th>
                                <th>MÃ£e</th>
                                <th>Pai</th>
                                <th style="width: 10%;" class="text-center">Status</th>
                                <th style="width: 12%;" class="text-center">AÃ§Ã£o</th>
                            </tr>
                        </thead>
                        <tbody id="tbody-vinculados-${oficinaId}">
                        </tbody>
                    </table>
                </div>
                <div class="alert alert-warning mb-0" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> 
                    <strong>Atenção:</strong> Desvincular removerá o aluno desta oficina, sendo necessário vinculá-lo novamente.
                </div>
                <button class="btn btn-secondary mt-3" onclick="voltarParaOficinas()">
                    <i class="fas fa-arrow-left"></i> Voltar para Oficinas
                </button>
            </div>
        </div>
    `;

    // Adiciona apas a tabela de oficinas
    const oficinasContainer = document.getElementById('oficinasContainer');
    oficinasContainer.parentNode.insertBefore(container, oficinasContainer.nextSibling);

    // Preenche a tabela com os alunos vinculados
    const tbody = document.getElementById(`tbody-vinculados-${oficinaId}`);
    alunos.forEach(aluno => {
        const tr = document.createElement('tr');
        tr.id = `vinculado-row-${aluno.id}`;

        const statusBadge = aluno.ativo === 'S' ?
            '<span class="badge bg-success">Ativo</span>' :
            '<span class="badge bg-danger">Inativo</span>';

        tr.innerHTML = `
            <td class="text-center">${aluno.id}</td>
            <td><strong>${aluno.nome}</strong></td>
            <td>${aluno.mae || 'N/A'}</td>
            <td>${aluno.pai || 'N/A'}</td>
            <td class="text-center">${statusBadge}</td>
            <td class="text-center">
                <button class="btn btn-danger btn-sm" 
                        onclick="desvincularAluno(${aluno.id}, ${oficinaId}, '${aluno.nome.replace(/'/g, "\\'")}', '${nomeOficina.replace(/'/g, "\\'")}')">
                    <i class="fas fa-unlink"></i> Desvincular
                </button>
            </td>
        `;

        tbody.appendChild(tr);
    });

    console.log('Alunos vinculados exibidos com sucesso!');
}

async function desvincularAluno(alunoId, ofertaId, nomeAluno, nomeOficina) {
    const confirma = confirm(
        `Tem certeza que deseja desvincular?\n\n` +
        `Aluno: ${nomeAluno}\n` +
        `Oficina: ${nomeOficina}`
    );

    if (!confirma) {
        console.log('Desvinculação cancelada pelo usuário');
        return;
    }

    if (isProcessing) {
        console.log('Aguarde, processando...');
        return;
    }

    isProcessing = true;
    const btn = event ? event.target : null;

    if (btn) {
        btn.disabled = true;
        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Aguarde...';
    }

    try {
        console.log(`Desvinculando aluno ${alunoId} da oferta ${ofertaId}...`);

        // ✅ USA O ID DA OFERTA DIRETAMENTE (sem mapeamento)
        const response = await fetch(`${API_BASE_URL}/aluno/${Number(alunoId)}/oficina/${Number(ofertaId)}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.mensagem || 'Erro ao desvincular aluno');
        }

        const result = await response.json();
        console.log('Resposta:', result);
        console.log('Desvinculação bem-sucedida!');

        alert('✅ Aluno desvinculado com sucesso!');

        // Remove a linha do aluno da tabela
        const row = document.getElementById(`vinculado-row-${alunoId}`);
        if (row) {
            row.remove();
        }

        // Verifica se ainda há alunos vinculados
        const tbody = document.getElementById(`tbody-vinculados-${ofertaId}`);
        if (tbody && tbody.children.length === 0) {
            alert('Não há mais alunos vinculados a esta oficina.');
            voltarParaOficinas();
        }

    } catch (error) {
        console.error('Erro:', error);
        alert('❌ Erro ao desvincular: ' + error.message);
    } finally {
        isProcessing = false;
        if (btn) {
            btn.disabled = false;
            btn.innerHTML = '<i class="fas fa-unlink"></i> Desvincular';
        }
    }
}

function voltarParaOficinas() {
    console.log('Voltando para lista de oficinas...');

    // Remove containers de alunos
    const containers = document.querySelectorAll('[id^="alunos-container-"], [id^="vinculados-container-"]');
    containers.forEach(c => c.remove());

    // Mostra todas as oficinas novamente
    const todasOficinas = document.querySelectorAll('[id^="oficina-row-"]');
    todasOficinas.forEach(row => row.style.display = '');

    console.log('Voltou para oficinas');
}

function formatarData(dataString) {
    if (!dataString) return 'N/A';

    try {
        // Formato: "2025-11-06" ou "Wed Nov 06 2025"
        const data = new Date(dataString);

        // Verifica se a data Ã© vÃ¡lida
        if (isNaN(data.getTime())) {
            return dataString;
        }

        return data.toLocaleDateString('pt-BR');
    } catch (e) {
        console.warn('Erro ao formatar data:', dataString);
        return dataString;
    }
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('Sistema de Associação Aluno-Oficina carregado');
    console.log('Clique em "Listar Oficinas" para começar');
});
