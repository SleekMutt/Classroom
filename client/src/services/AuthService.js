import { createBrowserHistory } from 'history';
import { BehaviorSubject } from 'rxjs';

const history = createBrowserHistory();

  
class AuthService {
  constructor() {
    this.isAuthenticatedSubject = new BehaviorSubject(this.isAuthenticated());
  }

  isAuthenticated() {
    return !!localStorage.getItem('accessToken');
  }

  toLogOut() {
    localStorage.removeItem('accessToken');
    this.isAuthenticatedSubject.next(false);
  }
  toSetAccesstoken(accessToken){
    localStorage.setItem('accessToken', accessToken);
    this.isAuthenticatedSubject.next(true);
  }
  
  navigateToGitHubOAuth() {
    history.push(
      'https://github.com/login/oauth/authorize?client_id=Iv1.bcdcaeb517916130'
    );
  }

  isAuthenticated$() {
    return this.isAuthenticatedSubject.asObservable();
  }
}

const authService = new AuthService();

export default authService;