$token = "656bf74d-bc05-4fb8-97dc-f5a66dfd956e"
$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Create project
$createProjectQuery = @{
    query = @"
mutation {
  projectCreate(input: {name: "SDP-Backend"}) {
    project {
      id
      name
    }
  }
}
"@
} | ConvertTo-Json

Write-Host "Creating Railway project..."
$response = Invoke-WebRequest -Uri "https://api.railway.app/graphql" -Method POST -Headers $headers -Body $createProjectQuery -ErrorAction SilentlyContinue

if ($response.StatusCode -eq 200) {
    Write-Host "Response: $($response.Content)"
    $projectData = $response.Content | ConvertFrom-Json
    $projectId = $projectData.data.projectCreate.project.id
    Write-Host "Project ID: $projectId"
} else {
    Write-Host "Error: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
}
