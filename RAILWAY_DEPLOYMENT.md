# Railway Deployment Guide for SDP Backend

## Automatic Deployment (Recommended)

### Step 1: Go to Railway Dashboard
- Visit: https://railway.app
- Sign in to your account

### Step 2: Create New Project
- Click "New Project"
- Select "Deploy from GitHub repo"
- Choose: `fsad-sdp-backend-ps45`

### Step 3: Configure Environment Variables
Once the project is created, add these environment variables in the Railway dashboard:

**Database Configuration:**
```
DB_URL=jdbc:mysql://YOUR_DB_HOST:YOUR_DB_PORT/YOUR_DB_NAME
DB_USERNAME=YOUR_DB_USERNAME
DB_PASSWORD=YOUR_DB_PASSWORD
```

**Email Configuration:**
```
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

**JWT Configuration:**
```
JWT_SECRET=your_32_character_secret_key_minimum_length_required
JWT_EXPIRATION_MS=600000
```

**CORS Configuration:**
```
CORS_ALLOWED_ORIGIN=https://your-frontend-domain.vercel.app
```

**Razorpay Configuration (Optional):**
```
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret
```

**JPA Configuration:**
```
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
```

### Step 4: Deploy
- Railway will automatically detect the Dockerfile
- Click "Deploy"
- Wait for build to complete
- Your backend URL will be generated automatically

## Files Already Configured

✅ **Dockerfile** - Uses Java 17 (Railway compatible)
✅ **railway.json** - Railway configuration
✅ **railway.toml** - Additional Railway config
✅ **Procfile** - Process configuration
✅ **Application Properties** - Configured to use environment variables

All files have been committed to your GitHub repository.

## Getting Your Variables

### Database
- Get from your MySQL host provider (Heroku, Render, etc.)

### Email (Gmail)
- Use an App Password: https://myaccount.google.com/apppasswords
- Generate a new 16-character password

### JWT Secret
- Generate a strong 32+ character string
- Example: `openssl rand -base64 32` (or use a random string generator)

### Frontend URL
- Your Vercel/deployed frontend domain

## After Deployment

Your backend will be available at:
```
https://your-project-name.railway.app
```

Use this URL in your frontend configuration as the API base URL.

## Troubleshooting

If deployment fails:
1. Check the build logs in Railway dashboard
2. Verify all environment variables are set
3. Ensure database is accessible from Railway
4. Check Java 17 is being used in the Docker build

## Support

For Railway issues: https://docs.railway.app
For project issues: Check your GitHub repository and Railway dashboard logs
