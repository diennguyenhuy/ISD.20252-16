import React, { useState } from 'react';
import { useNavigate } from 'react-router';
import { ShieldCheck, AlertCircle, ArrowLeft, Loader2 } from 'lucide-react';
import { useAuth } from '../../context/AuthContext'; // Nhớ kiểm tra lại đường dẫn import này
import { apiClient } from '../../api/client';       // Và đường dẫn import này cho đúng với dự án của bạn

export default function LoginSignup() {
  const navigate = useNavigate();
  const { login } = useAuth(); // Lấy hàm login từ Context

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault(); // Ngăn form tự reload trang
    setError('');
    setLoading(true);

    try {
      // 1. Gọi API Login thực tế xuống Spring Boot
      const response = await apiClient.post('/api/auth/login', {
        email,
        password
      });

      // 2. Lấy dữ liệu và Token xịn từ Backend
      const { token, id, username, email: userEmail, roles } = response.data;

      // 3. Ghi Token vào AuthContext
      login(token, { id, username, email: userEmail, roles });

      // 4. Điều hướng dựa trên Role (Tùy chọn)
      // Nếu bạn có phân chia trang admin riêng, có thể check role ở đây.
      // Tạm thời điều hướng tất cả vào /manager
      navigate('/manager');

    } catch (err: any) {
      // Bắt lỗi khi sai tài khoản/mật khẩu
      if (err.response && err.response.status === 401) {
        setError('Invalid credentials or unauthorized.');
      } else {
        setError(err.response?.data?.message || 'Server connection error.');
      }
    } finally {
      setLoading(false);
    }
  };

  // Nút hỗ trợ điền nhanh (Bạn nên đổi email này thành email thật đã tạo trong DB qua Postman)
  const fillManager = () => {
    setEmail('admin@aims.com'); // Thay bằng email bạn đã test đăng ký thành công ban nãy
    setPassword('123');         // Điền sẵn mật khẩu luôn cho tiện
  }

  return (
      <div className="min-h-screen bg-background flex flex-col items-center justify-center p-4 relative">
        <div className="w-full max-w-md bg-card rounded-3xl shadow-2xl border p-8 space-y-6 relative z-10">
          <div className="text-center">
            <ShieldCheck size={40} className="mx-auto text-primary mb-2" />
            <h2 className="text-2xl font-bold">System Login</h2>
          </div>

          <div className="bg-primary/5 p-3 rounded-lg flex gap-2">
            <button type="button" onClick={fillManager} className="flex-1 bg-card border rounded p-2 text-xs font-bold hover:bg-muted transition-colors">
              Fill Manager
            </button>
            <button type="button" onClick={() => setEmail('admin@aims.vn')} className="flex-1 bg-card border rounded p-2 text-xs font-bold hover:bg-muted transition-colors">
              Fill Admin
            </button>
          </div>

          {/* Thay thẻ div thành form để user có thể ấn Enter khi nhập xong mật khẩu */}
          <form onSubmit={handleLogin} className="space-y-4">
            <input
                type="email"
                required
                value={email}
                onChange={e => setEmail(e.target.value)}
                className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
                placeholder="Email"
            />
            <input
                type="password"
                required
                value={password}
                onChange={e => setPassword(e.target.value)}
                className="w-full border p-3 rounded-xl bg-input-background focus:ring-2 focus:ring-primary/50 outline-none transition-all"
                placeholder="Password"
            />

            {error && (
                <p className="text-destructive text-sm font-bold flex items-center gap-1.5">
                  <AlertCircle size={16}/> {error}
                </p>
            )}

            <button
                type="submit"
                disabled={loading}
                className="w-full bg-primary text-primary-foreground py-3 rounded-xl font-bold flex items-center justify-center gap-2 disabled:opacity-70 transition-all hover:bg-primary/90"
            >
              {loading && <Loader2 size={18} className="animate-spin" />}
              {loading ? 'Authenticating...' : 'Login'}
            </button>
          </form>

          <div className="text-center pt-4 mt-2 border-t border-border">
            <button
                type="button"
                onClick={() => navigate('/')}
                className="text-sm text-muted-foreground hover:text-primary font-bold transition-colors flex items-center justify-center gap-2 mx-auto"
            >
              <ArrowLeft size={16} /> Back to Store
            </button>
          </div>
        </div>
      </div>
  );
}