import Header from './header'

const layoutStyle = {
  margin: 10,
  padding: 10,
  border: '1px solid #DDD',
  background: '#e3e3e3'
}

class Layout extends React.Component {

  render () {
    return <div style={layoutStyle}>
             <Header />
             {this.props.children}
           </div>
  }
}

const withHeader = Page => <Layout>
                             <Page/>
                           </Layout>

export { Layout, withHeader }
