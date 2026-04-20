{{/*
Selector labels for bff-notes — used in selector.matchLabels and pod template labels.
These must never change after the first deploy (immutable in Kubernetes).
*/}}
{{- define "bff-notes.selectorLabels" -}}
app.kubernetes.io/name: {{ .Values.service.name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Full labels for bff-notes — used in metadata.labels of every resource.
Includes chart version for observability. Builds on top of selectorLabels.
*/}}
{{- define "bff-notes.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
{{- include "bff-notes.selectorLabels" . | nindent 0 }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}
