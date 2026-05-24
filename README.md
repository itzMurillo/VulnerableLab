# VulnerableLab - Sprint 02

Projeto Spring Boot utilizado como laboratório de segurança para identificar e corrigir vulnerabilidades comuns em APIs REST.

## Objetivo

Corrigir as fragilidades encontradas na Sprint 01 e aplicar a fase inicial de refatoração de segurança proposta no blueprint da Sprint 02.

## Principais correções

- Adição do Spring Security
- Autenticação com JWT assinado
- Senhas protegidas com BCrypt
- Rotas administrativas protegidas por perfil `ADMIN`
- Bloqueio de acesso não autorizado a endpoints sensíveis
- Correção de IDOR em consulta de usuários
- Correção de JPQL Injection com query parametrizada
- Sanitização de comentários contra XSS
- Uso de DTOs para evitar exposição de entidades
- Remoção de senha das respostas da API
- Separação de configurações por ambiente:
  - `application-dev.properties`
  - `application-prod.properties`
- Desativação do H2 Console
- Restrição de CORS
- Respostas de erro genéricas
- Headers de segurança
- Rate limiting simples
- Logs de auditoria para rotas sensíveis
